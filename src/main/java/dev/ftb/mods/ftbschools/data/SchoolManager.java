package dev.ftb.mods.ftbschools.data;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.block.SpawnMarkerBlock;
import dev.ftb.mods.ftbschools.kubejs.FTBSchoolsEvents;
import dev.ftb.mods.ftbschools.kubejs.LoadSchoolsEventJS;
import dev.ftb.mods.ftbschools.kubejs.SchoolEventJS;
import dev.ftb.mods.ftbschools.register.ModBlocks;
import dev.ftb.mods.ftbschools.structure.StructureBlockReplacerProcessor;
import dev.ftb.mods.ftbschools.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SchoolManager extends DataHolder {

    public static final ResourceKey<Level> DAY_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, FTBSchools.id("school_day"));
    public static final ResourceKey<Level> NIGHT_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, FTBSchools.id("school_night"));

    private static final Lazy<StructurePlaceSettings> settings = Lazy.of(() -> {
        StructurePlaceSettings settings = new StructurePlaceSettings();

        TagKey<Block> key = TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), FTBSchools.id("no_place"));
        List<Block> noPlace = ForgeRegistries.BLOCKS.tags().getTag(key).stream().toList();

        // important that the structure-block-replace processor runs *before* BlockIgnoreProcessor.STRUCTURE_AND_AIR !
        settings.addProcessor(StructureBlockReplacerProcessor.INSTANCE);
        settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        settings.addProcessor(new BlockIgnoreProcessor(Collections.singletonList(ModBlocks.SPAWN_MARKER.get())));
        if (!noPlace.isEmpty()) {
            settings.addProcessor(new BlockIgnoreProcessor(noPlace));
        }

        return settings;
    });

    public static final LevelResource FTBSCHOOLS_DATA = new LevelResource("ftbschools");

    public static SchoolManager INSTANCE;

    private final MinecraftServer server;
    public Map<ResourceLocation, SchoolType> schoolTypes;
    public ServerLevel dayDim;
    public ServerLevel nightDim;
    private Path filePath;

    private final List<SchoolData> daySchools;
    private final List<SchoolData> nightSchools;

    public SchoolManager(MinecraftServer server) {
        this.server = server;
        this.schoolTypes = new HashMap<>();
        this.daySchools = new ArrayList<>();
        this.nightSchools = new ArrayList<>();
    }

    public void loadAll() {
        FTBSchoolsEvents.LOAD_SCHOOLS.post(new LoadSchoolsEventJS((id, props) -> schoolTypes.put(id, new SchoolType(this, id, props))));

        FTBSchools.LOGGER.info("Loaded school types: " + schoolTypes);

        dayDim = server.getLevel(DAY_DIMENSION);
        nightDim = server.getLevel(NIGHT_DIMENSION);
        Path worldData = server.getWorldPath(FTBSCHOOLS_DATA);
        filePath = worldData.resolve("root.nbt");

        if (Files.exists(filePath)) {
            Util.tryIO(() -> load(NbtIo.readCompressed(filePath.toFile())));
        }
    }

    @Override
    protected void load(CompoundTag tag) {
        daySchools.clear();
        nightSchools.clear();

        ListTag d = tag.getList("DaySchools", 10);
        ListTag n = tag.getList("NightSchools", 10);

        for (int i = 0; i < d.size(); i++) {
            daySchools.add(new SchoolData(this, i, d.getCompound(i)));
        }

        for (int i = 0; i < n.size(); i++) {
            nightSchools.add(new SchoolData(this, i, n.getCompound(i)));
        }
    }

    public void saveAll() {
        CompoundTag rootTag = saveChanges();
        if (!rootTag.isEmpty()) {
            Util.getOrCreateDir(filePath.getParent());
            net.minecraft.Util.ioPool().execute(() -> Util.tryIO(() -> NbtIo.writeCompressed(rootTag, filePath.toFile())));
        }
    }

    @Override
    protected CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        ListTag d = new ListTag();
        ListTag n = new ListTag();

        for (SchoolData data : daySchools) {
            d.add(data.write());
        }

        for (SchoolData data : nightSchools) {
            n.add(data.write());
        }

        tag.put("DaySchools", d);
        tag.put("NightSchools", n);
        return tag;
    }

    public SchoolData add(SchoolType type) {
        SchoolData d = new SchoolData(this, (type.properties.night ? nightSchools : daySchools).size());
        (type.properties.night ? nightSchools : daySchools).add(d);
        markDirty();
        return d;
    }

    @Nullable
    public SchoolData get(boolean night, int index) {
        List<SchoolData> l = night ? nightSchools : daySchools;
        return index < 0 || index >= l.size() || l.get(index).type == null ? null : l.get(index);
    }

    @Nullable
    public SchoolData get(boolean night, int regionX, int regionZ) {
        return get(night, regionX + regionZ * 1024);
    }

    @Nullable
    public SchoolData currentSchool(ServerPlayer player) {
        UUID id = player.getUUID();

        for (SchoolData data : daySchools) {
            if (data.hasPlayer() && data.owner.equals(id)) {
                return data;
            }
        }

        for (SchoolData data : nightSchools) {
            if (data.hasPlayer() && data.owner.equals(id)) {
                return data;
            }
        }

        return null;
    }

    public void enterSchool(ServerPlayer player, SchoolType type) throws CommandSyntaxException {
        ResourceLocation id = type.id;
        StructureTemplate template = server.getStructureManager().get(id).orElse(null);

        if (template == null) {
            FTBSchools.LOGGER.error("School type has missing kubejs/data/" + id.getNamespace() + "/structures/" + id.getPath() + ".nbt!");
            return;
        }

        SchoolData previousSchool = currentSchool(player);

        SchoolData school = add(type);
        school.owner = player.getUUID();
        school.type = type;
        school.playerData = new CompoundTag();

        if (previousSchool != null) {
            school.playerData = previousSchool.playerData;
            previousSchool.playerData = null;
            FTBSchoolsEvents.LEAVE_SCHOOL.post(id.getNamespace() + "." + id.getPath(), new SchoolEventJS.Leave(school, player, true));
        } else {
            player.saveWithoutId(school.playerData);
        }

        if (player.getRespawnPosition() == null) {
            school.playerData.putBoolean("noRespawnPoint", true);
        }

        markDirty();

        player.getInventory().clearContent();
        player.removeAllEffects();
        player.giveExperienceLevels(Integer.MIN_VALUE);
        player.clearFire();
        player.setHealth(player.getMaxHealth());
        // TODO: Curios integration to clear any equipped curios

        BlockPos origin = school.getLocation();

        BlockPos spawnPos = null;
        Direction spawnFacing = Direction.NORTH;

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), ModBlocks.SPAWN_MARKER.get())) {
            spawnPos = info.pos;
            spawnFacing = info.state.getValue(SpawnMarkerBlock.SPAWN_FACING);
            break;
        }

        if (spawnPos == null) {
            FTBSchools.LOGGER.warn("School structure {} has no spawn point set! Players will spawnPos at the origin!", id);
            spawnPos = BlockPos.ZERO;
        }

        Vec3 spawnPosD = Vec3.atBottomCenterOf(origin.offset(spawnPos));
        float yRot = spawnFacing.toYRot();

        ServerLevel level = type.getDimension();

        template.placeInWorld(level, origin, origin, settings.get(), level.random, Block.UPDATE_CLIENTS);
        player.displayClientMessage(Component.literal("Successfully generated new school " + id + "/#" + school.index + " @ " + spawnPosD), false);
        player.teleportTo(level, spawnPosD.x, spawnPosD.y, spawnPosD.z, yRot, 0F);
        player.setRespawnPosition(level.dimension(), origin.offset(spawnPos), yRot, true, false);

        FTBSchoolsEvents.ENTER_SCHOOL.post(id.getNamespace() + "." + id.getPath(), new SchoolEventJS.Enter(school, player));

        markDirty();
    }

    public void leaveSchool(ServerPlayer player, boolean droppedOut) throws CommandSyntaxException {
        SchoolData data = currentSchool(player);
        if (data == null) {
            throw new SimpleCommandExceptionType(new LiteralMessage("You are not in a school!")).create();
        }

        CompoundTag tag = data.playerData;
        data.playerData = null;

        ResourceKey<Level> levelKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("Dimension")).result().orElse(Level.OVERWORLD);
        ListTag pos = tag.getList("Pos", 6);
        ListTag delta = tag.getList("Motion", 6);
        ListTag rot = tag.getList("Rotation", 5);

        int gameMode = tag.getInt("playerGameType");

        player.load(tag);

        if (tag.getBoolean("noRespawnPoint")) {
            player.setRespawnPosition(Level.OVERWORLD, null, 0.5f, false, false);
        }
        tag.remove("noRespawnPoint");

        player.teleportTo(server.getLevel(levelKey), pos.getDouble(0), pos.getDouble(1), pos.getDouble(2), rot.getFloat(0), rot.getFloat(1));
        player.setDeltaMovement(delta.getDouble(0), delta.getDouble(1), delta.getDouble(2));
        player.setGameMode(GameType.byId(gameMode, GameType.SURVIVAL));

        ResourceLocation id = data.type.id;

        FTBSchoolsEvents.LEAVE_SCHOOL.post(id.getNamespace() + "." + id.getPath(), new SchoolEventJS.Leave(data, player, droppedOut));
//        new SchoolEventJS.Leave(data, player, droppedOut)
//                .post(ScriptType.SERVER, FTBSchoolsEvents.LEAVE_SCHOOL, id.getNamespace() + "." + id.getPath());

    }
}

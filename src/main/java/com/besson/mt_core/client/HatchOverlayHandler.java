package com.besson.mt_core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class HatchOverlayHandler {

    public static void init() {
        NeoForge.EVENT_BUS.addListener(HatchOverlayHandler::onScan);
        HatchHighlightRenderer.init();
    }

    private static void onScan(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        ItemStack heldItem = mc.player.getMainHandItem();
        if (heldItem.isEmpty()) return;

        ResourceLocation normalTag = ResourceLocation.parse("mbd2:hatch");
        ResourceLocation advancedTag = ResourceLocation.parse("mbd2:advanced_hatch");

        boolean isNormalHatch = heldItem.is(ItemTags.create(normalTag));
        boolean isAdvancedHatch = heldItem.is(ItemTags.create(advancedTag));

        if (heldItem.getItem() instanceof BlockItem blockItem) {
            BlockState defaultState = blockItem.getBlock().defaultBlockState();
            if (defaultState.is(BlockTags.create(normalTag))) isNormalHatch = true;
            if (defaultState.is(BlockTags.create(advancedTag))) isAdvancedHatch = true;
        }

        if (!isNormalHatch && !isAdvancedHatch) return;

        if (!(mc.hitResult instanceof BlockHitResult blockHit)) return;

        BlockPos hitPos = blockHit.getBlockPos();
        BlockState hitState = mc.level.getBlockState(hitPos);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(hitState.getBlock());

        if (isAdvancedHatch && !HatchConfig.isAdvancedMachine(blockId)) {
            return;
        }

        List<BlockPos> relativePositions = HatchConfig.getPositions(blockId);

        if (!relativePositions.isEmpty()) {
            Direction controllerDir = hitState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    ? hitState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                    : Direction.NORTH;

            for (BlockPos relativePos : relativePositions) {
                BlockPos worldPos = toWorldPos(hitPos, controllerDir, relativePos);
                BlockState targetState = mc.level.getBlockState(worldPos);

                ResourceLocation targetBlockId = BuiltInRegistries.BLOCK.getKey(targetState.getBlock());
                String idString = targetBlockId.toString();

                if (targetState.isAir()
                        || targetState.canBeReplaced()
                        || idString.equals("kubejs:osm_machine_casing")
                        || idString.equals("kubejs:obs_machine_casing")
                        || idString.equals("kubejs:anti_machine_casing")
                ) {
                    HatchHighlightRenderer.enqueue(worldPos);
                }
            }
        }
    }

    public static BlockPos toWorldPos(BlockPos controllerPos, Direction controllerDir, BlockPos templatePos) {
        BlockPos rotatedPos;
        if (controllerDir == Direction.NORTH) {
            rotatedPos = new BlockPos(-templatePos.getX(), templatePos.getY(), templatePos.getZ());
        } else if (controllerDir == Direction.SOUTH) {
            rotatedPos = new BlockPos(templatePos.getX(), templatePos.getY(), -templatePos.getZ());
        } else if (controllerDir == Direction.EAST) {
            rotatedPos = new BlockPos(-templatePos.getZ(), templatePos.getY(), -templatePos.getX());
        } else { // WEST
            rotatedPos = new BlockPos(templatePos.getZ(), templatePos.getY(), templatePos.getX());
        }
        return rotatedPos.offset(controllerPos);
    }
}
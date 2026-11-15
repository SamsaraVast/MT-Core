package com.besson.mt_core.client;

import com.lowdragmc.lowdraglib2.client.renderer.IRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyMachineStateRenderer implements IRenderer {
    private final IRenderer blockRenderer;
    private final IRenderer frontRenderer;
    private final Direction frontFacing;
    private static final float FRONT_MODEL_OFFSET = 1 / 32f;
    private static final int VERTEX_STRIDE = 8;

    public MyMachineStateRenderer(IRenderer blockRenderer, IRenderer frontRenderer, Direction frontFacing) {
        this.blockRenderer = blockRenderer;
        this.frontRenderer = frontRenderer;
        this.frontFacing = frontFacing;
    }

    @Override
    public List<BakedQuad> renderModel(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
        if (side == frontFacing) {
            var blockQuads = blockRenderer.renderModel(level, pos, state, frontFacing, rand, data, renderType);
            var frontQuads = frontRenderer.renderModel(level, pos, state, side, rand, data, renderType);
            if (frontQuads.isEmpty()) {
                frontQuads = onlyFacingQuads(frontRenderer.renderModel(level, pos, state, null, rand, data, renderType), frontFacing);
            }
            if (blockQuads.isEmpty()) return offsetQuads(frontQuads, frontFacing, FRONT_MODEL_OFFSET);
            if (frontQuads.isEmpty()) return blockQuads;

            var quads = new ArrayList<BakedQuad>(blockQuads.size() + frontQuads.size());
            quads.addAll(blockQuads);
            quads.addAll(offsetQuads(frontQuads, frontFacing, FRONT_MODEL_OFFSET));
            return quads;
        }
        if (side == null) {
            return withoutFrontFacingQuads(blockRenderer.renderModel(level, pos, state, null, rand, data, renderType));
        }
        return blockRenderer.renderModel(level, pos, state, side, rand, data, renderType);
    }

    private List<BakedQuad> offsetQuads(List<BakedQuad> quads, Direction direction, float offset) {
        if (quads.isEmpty() || offset == 0) return quads;
        var shifted = new ArrayList<BakedQuad>(quads.size());
        for (var quad : quads) shifted.add(offsetQuad(quad, direction, offset));
        return shifted;
    }

    private BakedQuad offsetQuad(BakedQuad quad, Direction direction, float offset) {
        var vertices = quad.getVertices().clone();
        var xOffset = direction.getStepX() * offset;
        var yOffset = direction.getStepY() * offset;
        var zOffset = direction.getStepZ() * offset;
        for (var vertex = 0; vertex < 4; vertex++) {
            var base = vertex * VERTEX_STRIDE;
            vertices[base] = Float.floatToRawIntBits(Float.intBitsToFloat(vertices[base]) + xOffset);
            vertices[base + 1] = Float.floatToRawIntBits(Float.intBitsToFloat(vertices[base + 1]) + yOffset);
            vertices[base + 2] = Float.floatToRawIntBits(Float.intBitsToFloat(vertices[base + 2]) + zOffset);
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade(), quad.hasAmbientOcclusion());
    }

    private List<BakedQuad> onlyFacingQuads(List<BakedQuad> quads, Direction facing) {
        if (quads.isEmpty()) return quads;
        var filtered = new ArrayList<BakedQuad>(quads.size());
        for (var quad : quads) if (quad.getDirection() == facing) filtered.add(quad);
        return filtered.size() == quads.size() ? quads : filtered;
    }

    private List<BakedQuad> withoutFrontFacingQuads(List<BakedQuad> quads) {
        if (quads.isEmpty()) return quads;
        var filtered = new ArrayList<BakedQuad>(quads.size());
        for (var quad : quads) if (quad.getDirection() != frontFacing) filtered.add(quad);
        return filtered.size() == quads.size() ? quads : filtered;
    }
}
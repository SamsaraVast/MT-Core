package com.besson.mt_core.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Matrix4f;

import java.util.HashSet;
import java.util.Set;

public class HatchHighlightRenderer {

    private static final Set<BlockPos> HIGHLIGHT_QUEUE = new HashSet<>();

    public static void init() {
        NeoForge.EVENT_BUS.addListener(HatchHighlightRenderer::onRenderLevelStage);
    }

    public static void enqueue(BlockPos pos) {
        HIGHLIGHT_QUEUE.add(pos.immutable());
    }

    private static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || HIGHLIGHT_QUEUE.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        for (BlockPos pos : HIGHLIGHT_QUEUE) {
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            poseStack.translate(-0.005, -0.005, -0.005);
            poseStack.scale(1.01f, 1.01f, 1.01f);

            Matrix4f matrix = poseStack.last().pose();
            float r = 0.0f, g = 1.0f, b = 0.0f, a = 1.0f; // 绿色

            drawLine(consumer, matrix, 0,0,0, 1,0,0, r,g,b,a);
            drawLine(consumer, matrix, 1,0,0, 1,0,1, r,g,b,a);
            drawLine(consumer, matrix, 1,0,1, 0,0,1, r,g,b,a);
            drawLine(consumer, matrix, 0,0,1, 0,0,0, r,g,b,a);

            drawLine(consumer, matrix, 0,1,0, 1,1,0, r,g,b,a);
            drawLine(consumer, matrix, 1,1,0, 1,1,1, r,g,b,a);
            drawLine(consumer, matrix, 1,1,1, 0,1,1, r,g,b,a);
            drawLine(consumer, matrix, 0,1,1, 0,1,0, r,g,b,a);

            drawLine(consumer, matrix, 0,0,0, 0,1,0, r,g,b,a);
            drawLine(consumer, matrix, 1,0,0, 1,1,0, r,g,b,a);
            drawLine(consumer, matrix, 1,0,1, 1,1,1, r,g,b,a);
            drawLine(consumer, matrix, 0,0,1, 0,1,1, r,g,b,a);

            poseStack.popPose();
        }

        poseStack.popPose();

        bufferSource.endBatch(RenderType.lines());
        HIGHLIGHT_QUEUE.clear();
    }

    private static void drawLine(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {

        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;


        float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        float nx = dx / len;
        float ny = dy / len;
        float nz = dz / len;


        consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setNormal(nx, ny, nz);
        consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setNormal(nx, ny, nz);
    }
}
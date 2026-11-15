package com.besson.mt_core.client;

import com.lowdragmc.lowdraglib2.client.renderer.IRenderer;
import com.lowdragmc.mbd2.api.machine.IMachine;
import com.lowdragmc.mbd2.common.machine.MBDMachine;
import com.lowdragmc.mbd2.common.machine.MBDPartMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartHatchRenderer implements IRenderer {

    private final IRenderer originalRenderer;

    public SmartHatchRenderer(IRenderer originalRenderer) {
        this.originalRenderer = originalRenderer;
    }

    //物品输入/输出仓
    public static final ModelResourceLocation item_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/item_input"), "standalone");
    public static final ModelResourceLocation item_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/item_output"), "standalone");
    public static final ModelResourceLocation super_item_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_item_input"), "standalone");
    public static final ModelResourceLocation super_item_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_item_output"), "standalone");
    public static final ModelResourceLocation final_item_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_item_input"), "standalone");
    public static final ModelResourceLocation final_item_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_item_output"), "standalone");

    //流体输入/输出仓
    public static final ModelResourceLocation fluid_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/fluid_input"), "standalone");
    public static final ModelResourceLocation fluid_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/fluid_output"), "standalone");
    public static final ModelResourceLocation super_fluid_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_fluid_input"), "standalone");
    public static final ModelResourceLocation super_fluid_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_fluid_output"), "standalone");
    public static final ModelResourceLocation final_fluid_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_fluid_input"), "standalone");
    public static final ModelResourceLocation final_fluid_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_fluid_output"), "standalone");

    //化学品输入/输出仓
    public static final ModelResourceLocation gas_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/gas_input"), "standalone");
    public static final ModelResourceLocation gas_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/gas_output"), "standalone");
    public static final ModelResourceLocation super_gas_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_gas_input"), "standalone");
    public static final ModelResourceLocation super_gas_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_gas_output"), "standalone");
    public static final ModelResourceLocation final_gas_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_gas_input"), "standalone");
    public static final ModelResourceLocation final_gas_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_gas_output"), "standalone");

    //集成输入/输出仓
    public static final ModelResourceLocation all_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/all_input"), "standalone");
    public static final ModelResourceLocation all_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/all_output"), "standalone");
    public static final ModelResourceLocation super_all_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_all_input"), "standalone");
    public static final ModelResourceLocation super_all_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_all_output"), "standalone");
    public static final ModelResourceLocation final_all_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_all_input"), "standalone");
    public static final ModelResourceLocation final_all_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_all_output"), "standalone");

    //能量输入/输出仓
    public static final ModelResourceLocation fe_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/fe_input"), "standalone");
    public static final ModelResourceLocation fe_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/fe_output"), "standalone");
    public static final ModelResourceLocation super_fe_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_fe_input"), "standalone");
    public static final ModelResourceLocation super_fe_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_fe_output"), "standalone");
    public static final ModelResourceLocation final_fe_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_fe_input"), "standalone");
    public static final ModelResourceLocation final_fe_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/final_fe_output"), "standalone");

    //ae2接口输入/输出仓/样板仓
    public static final ModelResourceLocation simple_ae2_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/simple_ae2_input"), "standalone");
    public static final ModelResourceLocation simple_ae2_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/simple_ae2_output"), "standalone");
    public static final ModelResourceLocation ae2_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/ae2_input"), "standalone");
    public static final ModelResourceLocation ae2_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/ae2_output"), "standalone");
    public static final ModelResourceLocation super_ae2_input = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_ae2_input"), "standalone");
    public static final ModelResourceLocation super_ae2_output = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_ae2_output"), "standalone");
    //特殊仓室
    public static final ModelResourceLocation simple_prl_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/simple_prl_hatch"), "standalone");
    public static final ModelResourceLocation prl_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/prl_hatch"), "standalone");
    public static final ModelResourceLocation super_prl_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_prl_hatch"), "standalone");

    public static final ModelResourceLocation simple_speed_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/simple_speed_hatch"), "standalone");
    public static final ModelResourceLocation speed_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/speed_hatch"), "standalone");
    public static final ModelResourceLocation super_speed_hatch = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_speed_hatch"), "standalone");

    public static final ModelResourceLocation simple_ae2_pattern = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/simple_ae2_pattern"), "standalone");
    public static final ModelResourceLocation ae2_pattern = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/ae2_pattern"), "standalone");
    public static final ModelResourceLocation super_ae2_pattern = new ModelResourceLocation(ResourceLocation.parse("kubejs:block/super_ae2_pattern"), "standalone");

    public static final Set<String> INPUT_PARTS = new HashSet<>();
    public static final Set<String> OUTPUT_PARTS = new HashSet<>();
    public static final Set<String> HATCH_PARTS = new HashSet<>();

    static {
        //输入仓
        INPUT_PARTS.add("mbd2:item_input");
        INPUT_PARTS.add("mbd2:super_item_input");
        INPUT_PARTS.add("mbd2:final_item_input");

        INPUT_PARTS.add("mbd2:fluid_input");
        INPUT_PARTS.add("mbd2:super_fluid_input");
        INPUT_PARTS.add("mbd2:final_fluid_input");

        INPUT_PARTS.add("mbd2:gas_input");
        INPUT_PARTS.add("mbd2:super_gas_input");
        INPUT_PARTS.add("mbd2:final_gas_input");

        INPUT_PARTS.add("mbd2:all_input");
        INPUT_PARTS.add("mbd2:super_all_input");
        INPUT_PARTS.add("mbd2:final_all_input");

        INPUT_PARTS.add("mbd2:fe_input");
        INPUT_PARTS.add("mbd2:super_fe_input");
        INPUT_PARTS.add("mbd2:final_fe_input");

        INPUT_PARTS.add("mbd2:simple_ae2_input");
        INPUT_PARTS.add("mbd2:ae2_input");
        INPUT_PARTS.add("mbd2:super_ae2_input");

        //输出仓
        OUTPUT_PARTS.add("mbd2:item_output");
        OUTPUT_PARTS.add("mbd2:super_item_output");
        OUTPUT_PARTS.add("mbd2:final_item_output");

        OUTPUT_PARTS.add("mbd2:fluid_output");
        OUTPUT_PARTS.add("mbd2:super_fluid_output");
        OUTPUT_PARTS.add("mbd2:final_fluid_output");

        OUTPUT_PARTS.add("mbd2:gas_output");
        OUTPUT_PARTS.add("mbd2:super_gas_output");
        OUTPUT_PARTS.add("mbd2:final_gas_output");

        OUTPUT_PARTS.add("mbd2:all_output");
        OUTPUT_PARTS.add("mbd2:super_all_output");
        OUTPUT_PARTS.add("mbd2:final_all_output");

        OUTPUT_PARTS.add("mbd2:fe_output");
        OUTPUT_PARTS.add("mbd2:super_fe_output");
        OUTPUT_PARTS.add("mbd2:final_fe_output");

        OUTPUT_PARTS.add("mbd2:simple_ae2_output");
        OUTPUT_PARTS.add("mbd2:ae2_output");
        OUTPUT_PARTS.add("mbd2:super_ae2_output");

        //特殊仓室
        HATCH_PARTS.add("mbd2:simple_prl_hatch");
        HATCH_PARTS.add("mbd2:prl_hatch");
        HATCH_PARTS.add("mbd2:super_prl_hatch");

        HATCH_PARTS.add("mbd2:simple_speed_hatch");
        HATCH_PARTS.add("mbd2:speed_hatch");
        HATCH_PARTS.add("mbd2:super_speed_hatch");

        HATCH_PARTS.add("mbd2:simple_ae2_pattern");
        HATCH_PARTS.add("mbd2:ae2_pattern");
        HATCH_PARTS.add("mbd2:super_ae2_pattern");
    }

    private List<BakedQuad> getQuadsFromModel(ModelResourceLocation loc, @Nullable Direction side, RandomSource rand, ModelData data) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(loc);
        if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
            return new ArrayList<>();
        }
        return model.getQuads(null, side, rand, data, RenderType.translucent());
    }

    @Override
    public List<BakedQuad> renderModel(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {

        if (level == null || pos == null) {
            return originalRenderer.renderModel(level, pos, state, side, rand, data, renderType);
        }

        IMachine machine = IMachine.ofMachine(level, pos).orElse(null);

        if (machine instanceof MBDPartMachine partMachine) {
            if (partMachine.isFormed()) {
                for (com.lowdragmc.mbd2.api.machine.IMultiController controller : partMachine.getControllers()) {
                    if (controller != null && controller.isFormed()) {
                        var mainMachineOpt = IMachine.ofMachine(level, controller.getPos());
                        if (mainMachineOpt.isPresent() && mainMachineOpt.get() instanceof MBDMachine mainMachine) {

                            String mainMachineId = mainMachine.getDefinition().id().toString();
                            String partMachineId = partMachine.getDefinition().id().toString();

                            //反物质钢材质
                            switch (mainMachineId) {
                                case "mbd2:yztcq":
                                case "mbd2:cjfyd":
                                case "mbd2:create":
                                case "mbd2:hxnq":

                                    switch (partMachineId) {

                                        //物品仓
                                        case "mbd2:item_input": return getQuadsFromModel(final_item_input, side, rand, data);
                                        case "mbd2:item_output": return getQuadsFromModel(final_item_output, side, rand, data);
                                        case "mbd2:super_item_input": return getQuadsFromModel(final_item_input, side, rand, data);
                                        case "mbd2:super_item_output": return getQuadsFromModel(final_item_output, side, rand, data);
                                        case "mbd2:final_item_input": return getQuadsFromModel(final_item_input, side, rand, data);
                                        case "mbd2:final_item_output": return getQuadsFromModel(final_item_output, side, rand, data);

                                        //流体仓
                                        case "mbd2:fluid_input": return getQuadsFromModel(final_fluid_input, side, rand, data);
                                        case "mbd2:fluid_output": return getQuadsFromModel(final_fluid_output, side, rand, data);
                                        case "mbd2:super_fluid_input": return getQuadsFromModel(final_fluid_input, side, rand, data);
                                        case "mbd2:super_fluid_output": return getQuadsFromModel(final_fluid_output, side, rand, data);
                                        case "mbd2:final_fluid_input": return getQuadsFromModel(final_fluid_input, side, rand, data);
                                        case "mbd2:final_fluid_output": return getQuadsFromModel(final_fluid_output, side, rand, data);

                                        //化学品仓
                                        case "mbd2:gas_input": return getQuadsFromModel(final_gas_input, side, rand, data);
                                        case "mbd2:gas_output": return getQuadsFromModel(final_gas_output, side, rand, data);
                                        case "mbd2:super_gas_input": return getQuadsFromModel(final_gas_input, side, rand, data);
                                        case "mbd2:super_gas_output": return getQuadsFromModel(final_gas_output, side, rand, data);
                                        case "mbd2:final_gas_input": return getQuadsFromModel(final_gas_input, side, rand, data);
                                        case "mbd2:final_gas_output": return getQuadsFromModel(final_gas_output, side, rand, data);

                                        //集成仓
                                        case "mbd2:all_input": return getQuadsFromModel(final_all_input, side, rand, data);
                                        case "mbd2:all_output": return getQuadsFromModel(final_all_output, side, rand, data);
                                        case "mbd2:super_all_input": return getQuadsFromModel(final_all_input, side, rand, data);
                                        case "mbd2:super_all_output": return getQuadsFromModel(final_all_output, side, rand, data);
                                        case "mbd2:final_all_input": return getQuadsFromModel(final_all_input, side, rand, data);
                                        case "mbd2:final_all_output": return getQuadsFromModel(final_all_output, side, rand, data);

                                        //能量仓
                                        case "mbd2:fe_input": return getQuadsFromModel(final_fe_input, side, rand, data);
                                        case "mbd2:fe_output": return getQuadsFromModel(final_fe_output, side, rand, data);
                                        case "mbd2:super_fe_input": return getQuadsFromModel(final_fe_input, side, rand, data);
                                        case "mbd2:super_fe_output": return getQuadsFromModel(final_fe_output, side, rand, data);
                                        case "mbd2:final_fe_input": return getQuadsFromModel(final_fe_input, side, rand, data);
                                        case "mbd2:final_fe_output": return getQuadsFromModel(final_fe_output, side, rand, data);

                                        //特殊仓室
                                        case "mbd2:simple_prl_hatch": return getQuadsFromModel(super_prl_hatch, side, rand, data);
                                        case "mbd2:prl_hatch": return getQuadsFromModel(super_prl_hatch, side, rand, data);
                                        case "mbd2:super_prl_hatch": return getQuadsFromModel(super_prl_hatch, side, rand, data);

                                        case "mbd2:simple_speed_hatch": return getQuadsFromModel(super_speed_hatch, side, rand, data);
                                        case "mbd2:speed_hatch": return getQuadsFromModel(super_speed_hatch, side, rand, data);
                                        case "mbd2:super_speed_hatch": return getQuadsFromModel(super_speed_hatch, side, rand, data);

                                        case "mbd2:ae2_pattern": return getQuadsFromModel(super_ae2_pattern, side, rand, data);

                                        case "mbd2:simple_ae2_input": return getQuadsFromModel(super_ae2_input, side, rand, data);
                                        case "mbd2:simple_ae2_output": return getQuadsFromModel(super_ae2_output, side, rand, data);
                                        case "mbd2:ae2_input": return getQuadsFromModel(super_ae2_input, side, rand, data);
                                        case "mbd2:ae2_output": return getQuadsFromModel(super_ae2_output, side, rand, data);
                                        case "mbd2:super_ae2_input": return getQuadsFromModel(super_ae2_input, side, rand, data);
                                        case "mbd2:super_ae2_output": return getQuadsFromModel(super_ae2_output, side, rand, data);
                                    }
                                    break;

                                //强化黑曜石材质
                                case "mbd2:bee":
                                case "mbd2:super_dlzz":
                                case "mbd2:super_xkckj":
                                case "mbd2:hjyll":
                                case "mbd2:super_hjyll":
                                case "mbd2:super_jbysj":
                                case "mbd2:jbysj":
                                case "mbd2:super_swmn":
                                case "mbd2:wznqy":
                                case "mbd2:super_lqj":

                                    switch (partMachineId) {
                                        //物品仓
                                        case "mbd2:item_input": return getQuadsFromModel(super_item_input, side, rand, data);
                                        case "mbd2:item_output": return getQuadsFromModel(super_item_output, side, rand, data);
                                        case "mbd2:super_item_input": return getQuadsFromModel(super_item_input, side, rand, data);
                                        case "mbd2:super_item_output": return getQuadsFromModel(super_item_output, side, rand, data);
                                        case "mbd2:final_item_input": return getQuadsFromModel(super_item_input, side, rand, data);
                                        case "mbd2:final_item_output": return getQuadsFromModel(super_item_output, side, rand, data);

                                        //流体仓
                                        case "mbd2:fluid_input": return getQuadsFromModel(super_fluid_input, side, rand, data);
                                        case "mbd2:fluid_output": return getQuadsFromModel(super_fluid_output, side, rand, data);
                                        case "mbd2:super_fluid_input": return getQuadsFromModel(super_fluid_input, side, rand, data);
                                        case "mbd2:super_fluid_output": return getQuadsFromModel(super_fluid_output, side, rand, data);
                                        case "mbd2:final_fluid_input": return getQuadsFromModel(super_fluid_input, side, rand, data);
                                        case "mbd2:final_fluid_output": return getQuadsFromModel(super_fluid_output, side, rand, data);

                                        //化学品仓
                                        case "mbd2:gas_input": return getQuadsFromModel(super_gas_input, side, rand, data);
                                        case "mbd2:gas_output": return getQuadsFromModel(super_gas_output, side, rand, data);
                                        case "mbd2:super_gas_input": return getQuadsFromModel(super_gas_input, side, rand, data);
                                        case "mbd2:super_gas_output": return getQuadsFromModel(super_gas_output, side, rand, data);
                                        case "mbd2:final_gas_input": return getQuadsFromModel(super_gas_input, side, rand, data);
                                        case "mbd2:final_gas_output": return getQuadsFromModel(super_gas_output, side, rand, data);

                                        //集成仓
                                        case "mbd2:all_input": return getQuadsFromModel(super_all_input, side, rand, data);
                                        case "mbd2:all_output": return getQuadsFromModel(super_all_output, side, rand, data);
                                        case "mbd2:super_all_input": return getQuadsFromModel(super_all_input, side, rand, data);
                                        case "mbd2:super_all_output": return getQuadsFromModel(super_all_output, side, rand, data);
                                        case "mbd2:final_all_input": return getQuadsFromModel(super_all_input, side, rand, data);
                                        case "mbd2:final_all_output": return getQuadsFromModel(super_all_output, side, rand, data);

                                        //能量仓
                                        case "mbd2:fe_input": return getQuadsFromModel(super_fe_input, side, rand, data);
                                        case "mbd2:fe_output": return getQuadsFromModel(super_fe_output, side, rand, data);
                                        case "mbd2:super_fe_input": return getQuadsFromModel(super_fe_input, side, rand, data);
                                        case "mbd2:super_fe_output": return getQuadsFromModel(super_fe_output, side, rand, data);
                                        case "mbd2:final_fe_input": return getQuadsFromModel(super_fe_input, side, rand, data);
                                        case "mbd2:final_fe_output": return getQuadsFromModel(super_fe_output, side, rand, data);

                                        //特殊仓室
                                        case "mbd2:simple_prl_hatch": return getQuadsFromModel(prl_hatch, side, rand, data);
                                        case "mbd2:prl_hatch": return getQuadsFromModel(prl_hatch, side, rand, data);
                                        case "mbd2:super_prl_hatch": return getQuadsFromModel(prl_hatch, side, rand, data);

                                        case "mbd2:simple_speed_hatch": return getQuadsFromModel(speed_hatch, side, rand, data);
                                        case "mbd2:speed_hatch": return getQuadsFromModel(speed_hatch, side, rand, data);
                                        case "mbd2:super_speed_hatch": return getQuadsFromModel(speed_hatch, side, rand, data);

                                        case "mbd2:ae2_pattern": return getQuadsFromModel(ae2_pattern, side, rand, data);

                                        case "mbd2:simple_ae2_input": return getQuadsFromModel(ae2_input, side, rand, data);
                                        case "mbd2:simple_ae2_output": return getQuadsFromModel(ae2_output, side, rand, data);
                                        case "mbd2:ae2_input": return getQuadsFromModel(ae2_input, side, rand, data);
                                        case "mbd2:ae2_output": return getQuadsFromModel(ae2_output, side, rand, data);
                                        case "mbd2:super_ae2_input": return getQuadsFromModel(ae2_input, side, rand, data);
                                        case "mbd2:super_ae2_output": return getQuadsFromModel(ae2_output, side, rand, data);
                                    }
                                    break;

                                //锇材质
                                case "mbd2:dlzz":
                                case "mbd2:swmn":
                                case "mbd2:xkckj":
                                case "mbd2:hjfst":
                                case "mbd2:lqj":

                                    switch (partMachineId) {
                                        //物品仓
                                        case "mbd2:item_input": return getQuadsFromModel(item_input, side, rand, data);
                                        case "mbd2:item_output": return getQuadsFromModel(item_output, side, rand, data);
                                        case "mbd2:super_item_input": return getQuadsFromModel(item_input, side, rand, data);
                                        case "mbd2:super_item_output": return getQuadsFromModel(item_output, side, rand, data);
                                        case "mbd2:final_item_input": return getQuadsFromModel(item_input, side, rand, data);
                                        case "mbd2:final_item_output": return getQuadsFromModel(item_output, side, rand, data);

                                        //流体仓
                                        case "mbd2:fluid_input": return getQuadsFromModel(fluid_input, side, rand, data);
                                        case "mbd2:fluid_output": return getQuadsFromModel(fluid_output, side, rand, data);
                                        case "mbd2:super_fluid_input": return getQuadsFromModel(fluid_input, side, rand, data);
                                        case "mbd2:super_fluid_output": return getQuadsFromModel(fluid_output, side, rand, data);
                                        case "mbd2:final_fluid_input": return getQuadsFromModel(fluid_input, side, rand, data);
                                        case "mbd2:final_fluid_output": return getQuadsFromModel(fluid_output, side, rand, data);

                                        //化学品仓
                                        case "mbd2:gas_input": return getQuadsFromModel(gas_input, side, rand, data);
                                        case "mbd2:gas_output": return getQuadsFromModel(gas_output, side, rand, data);
                                        case "mbd2:super_gas_input": return getQuadsFromModel(gas_input, side, rand, data);
                                        case "mbd2:super_gas_output": return getQuadsFromModel(gas_output, side, rand, data);
                                        case "mbd2:final_gas_input": return getQuadsFromModel(gas_input, side, rand, data);
                                        case "mbd2:final_gas_output": return getQuadsFromModel(gas_output, side, rand, data);

                                        //集成仓
                                        case "mbd2:all_input": return getQuadsFromModel(all_input, side, rand, data);
                                        case "mbd2:all_output": return getQuadsFromModel(all_output, side, rand, data);
                                        case "mbd2:super_all_input": return getQuadsFromModel(all_input, side, rand, data);
                                        case "mbd2:super_all_output": return getQuadsFromModel(all_output, side, rand, data);
                                        case "mbd2:final_all_input": return getQuadsFromModel(all_input, side, rand, data);
                                        case "mbd2:final_all_output": return getQuadsFromModel(all_output, side, rand, data);

                                        //能量仓
                                        case "mbd2:fe_input": return getQuadsFromModel(fe_input, side, rand, data);
                                        case "mbd2:fe_output": return getQuadsFromModel(fe_output, side, rand, data);
                                        case "mbd2:super_fe_input": return getQuadsFromModel(fe_input, side, rand, data);
                                        case "mbd2:super_fe_output": return getQuadsFromModel(fe_output, side, rand, data);
                                        case "mbd2:final_fe_input": return getQuadsFromModel(fe_input, side, rand, data);
                                        case "mbd2:final_fe_output": return getQuadsFromModel(fe_output, side, rand, data);

                                        //特殊仓室
                                        case "mbd2:simple_prl_hatch": return getQuadsFromModel(simple_prl_hatch, side, rand, data);
                                        case "mbd2:prl_hatch": return getQuadsFromModel(simple_prl_hatch, side, rand, data);
                                        case "mbd2:super_prl_hatch": return getQuadsFromModel(simple_prl_hatch, side, rand, data);

                                        case "mbd2:simple_speed_hatch": return getQuadsFromModel(simple_speed_hatch, side, rand, data);
                                        case "mbd2:speed_hatch": return getQuadsFromModel(simple_speed_hatch, side, rand, data);
                                        case "mbd2:super_speed_hatch": return getQuadsFromModel(simple_speed_hatch, side, rand, data);

                                        case "mbd2:ae2_pattern": return getQuadsFromModel(simple_ae2_pattern, side, rand, data);

                                        case "mbd2:simple_ae2_input": return getQuadsFromModel(simple_ae2_input, side, rand, data);
                                        case "mbd2:simple_ae2_output": return getQuadsFromModel(simple_ae2_output, side, rand, data);
                                        case "mbd2:ae2_input": return getQuadsFromModel(simple_ae2_input, side, rand, data);
                                        case "mbd2:ae2_output": return getQuadsFromModel(simple_ae2_output, side, rand, data);
                                        case "mbd2:super_ae2_input": return getQuadsFromModel(simple_ae2_input, side, rand, data);
                                        case "mbd2:super_ae2_output": return getQuadsFromModel(simple_ae2_output, side, rand, data);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return originalRenderer.renderModel(level, pos, state, side, rand, data, renderType);
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        originalRenderer.renderItem(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource rand, ModelData modelData) {
        return ChunkRenderTypeSet.union(
                originalRenderer.getRenderTypes(level, pos, state, rand, modelData),
                ChunkRenderTypeSet.of(RenderType.translucent())
        );
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, ModelData modelData) {
        return Minecraft.getInstance().getModelManager().getModel(item_input).getParticleIcon(modelData);
    }
}
package com.besson.mt_core.client;

import com.lowdragmc.mbd2.common.machine.definition.MBDMachineDefinition;
import com.lowdragmc.mbd2.common.machine.definition.config.MachineState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = "mt_core", value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        try {
            for (MBDMachineDefinition definition : com.lowdragmc.mbd2.api.registry.MBDRegistries.MACHINE_DEFINITIONS.values()) {
                String id = definition.id().toString();

                    if (id.equals("mbd2:item_input")||
                        id.equals("mbd2:super_item_input")||
                        id.equals("mbd2:final_item_input")||

                        id.equals("mbd2:item_output")||
                        id.equals("mbd2:super_item_output")||
                        id.equals("mbd2:final_item_output")||

                        id.equals("mbd2:fluid_input")||
                        id.equals("mbd2:super_fluid_input")||
                        id.equals("mbd2:final_fluid_input")||

                        id.equals("mbd2:fluid_output")||
                        id.equals("mbd2:super_fluid_output")||
                        id.equals("mbd2:final_fluid_output")||

                        id.equals("mbd2:gas_input")||
                        id.equals("mbd2:super_gas_input")||
                        id.equals("mbd2:final_gas_input")||

                        id.equals("mbd2:gas_output")||
                        id.equals("mbd2:super_gas_output")||
                        id.equals("mbd2:final_gas_output")||

                        id.equals("mbd2:all_input")||
                        id.equals("mbd2:super_all_input")||
                        id.equals("mbd2:final_all_input")||

                        id.equals("mbd2:all_output")||
                        id.equals("mbd2:super_all_output")||
                        id.equals("mbd2:final_all_output")||

                        id.equals("mbd2:fe_input")||
                        id.equals("mbd2:super_fe_input")||
                        id.equals("mbd2:final_fe_input")||

                        id.equals("mbd2:fe_output")||
                        id.equals("mbd2:super_fe_output")||
                        id.equals("mbd2:final_fe_output")||

                        id.equals("mbd2:simple_ae2_input")||
                        id.equals("mbd2:ae2_input")||
                        id.equals("mbd2:super_ae2_input")||

                        id.equals("mbd2:simple_ae2_output")||
                        id.equals("mbd2:ae2_output")||
                        id.equals("mbd2:super_ae2_output")||

                        id.equals("mbd2:simple_prl_hatch")||
                        id.equals("mbd2:prl_hatch")||
                        id.equals("mbd2:super_prl_hatch")||

                        id.equals("mbd2:simple_speed_hatch")||
                        id.equals("mbd2:speed_hatch")||
                        id.equals("mbd2:super_speed_hatch")||

                        id.equals("mbd2:simple_ae2_pattern")||
                        id.equals("mbd2:ae2_pattern")||
                        id.equals("mbd2:super_ae2_pattern")

                    )
                    {

                    MachineState baseState = definition.stateMachine().getRootState();
                    var toggle = baseState.renderer();

                    if (toggle != null && !(toggle.getValue() instanceof SmartHatchRenderer)) {
                        toggle.setValue(new SmartHatchRenderer(toggle.getValue()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("MT Core: 替换 MBD2 渲染器失败 " + e.getMessage());
        }
    }
}
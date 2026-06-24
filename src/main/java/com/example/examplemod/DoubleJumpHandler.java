package com.example.examplemod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Двойной прыжок целиком на сервере: клиент уже шлёт своё нажатие прыжка
// каждый тик (ServerboundPlayerInputPacket) -> читаем его в server tick.
// Проверка ботинок тут авторитетна (клиент не подделает), отдельный пакет не нужен.
@EventBusSubscriber(modid = ExampleMod20.MODID)
public class DoubleJumpHandler {

    // Сколько тиков нужно провисеть в воздухе после первого прыжка,
    // прежде чем разрешить двойной. Иначе импульс складывается с восходящей
    // скоростью первого прыжка и ощущается как jump boost, а не двойной прыжок.
    private static final int MIN_AIR_TICKS = 6; // ~0.3 c, подкрути под себя

    // Состояние на игрока: было ли нажатие в прошлом тике + израсходован ли прыжок
    // + сколько тиков подряд игрок в воздухе.
    private static final Set<UUID> jumpWasDown = new HashSet<>();
    private static final Set<UUID> usedDoubleJump = new HashSet<>();
    private static final Map<UUID, Integer> airTicks = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();

            // На земле сбрасываем — двойной прыжок снова доступен, счётчик воздуха в 0.
            if (player.onGround()) {
                usedDoubleJump.remove(id);
                airTicks.put(id, 0);
            } else {
                airTicks.merge(id, 1, Integer::sum);
            }

            boolean jumpDown = player.getLastClientInput().jump();
            boolean justPressed = jumpDown && !jumpWasDown.contains(id); // фронт нажатия
            if (jumpDown) jumpWasDown.add(id); else jumpWasDown.remove(id);

            if (!justPressed) continue;
            if (player.onGround() || usedDoubleJump.contains(id)) continue; // первый прыжок обычный
            if (airTicks.getOrDefault(id, 0) < MIN_AIR_TICKS) continue;     // мини-отступ от первого прыжка
            if (player.getAbilities().flying) continue;

            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (!Boolean.TRUE.equals(boots.get(ModDataComponents.DOUBLE_JUMP.get()))) continue;

            Vec3 v = player.getDeltaMovement();
            player.setDeltaMovement(v.x, 0.55, v.z); // высоту подкрути под себя
            player.hurtMarked = true;                // форсим синк новой скорости на клиент
            usedDoubleJump.add(id);
        }
    }
}

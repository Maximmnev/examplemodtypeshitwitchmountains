package com.example.examplemod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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

// Двойной (тройной, ...) прыжок целиком на сервере: клиент уже шлёт своё нажатие
// прыжка каждый тик (ServerboundPlayerInputPacket) -> читаем его в server tick.
// Кол-во доп. прыжков берём из атрибута air_jumps (его даёт модификатор с ботинок),
// поэтому проверка авторитетна и отдельный пакет не нужен.
@EventBusSubscriber(modid = ExampleMod20.MODID)
public class DoubleJumpHandler {

    // Сколько тиков нужно провисеть в воздухе после прыжка, прежде чем разрешить
    // следующий. Иначе импульс складывается с восходящей скоростью и ощущается как jump boost.
    private static final int MIN_AIR_TICKS = 6; // ~0.3 c, подкрути под себя

    private static final Set<UUID> jumpWasDown = new HashSet<>();
    private static final Map<UUID, Integer> jumpsUsed = new HashMap<>(); // израсходовано доп. прыжков за полёт
    private static final Map<UUID, Integer> airTicks = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();

            // На земле сбрасываем — доп. прыжки снова доступны, счётчик воздуха в 0.
            if (player.onGround()) {
                jumpsUsed.put(id, 0);
                airTicks.put(id, 0);
            } else {
                airTicks.merge(id, 1, Integer::sum);
            }

            boolean jumpDown = player.getLastClientInput().jump();
            boolean justPressed = jumpDown && !jumpWasDown.contains(id); // фронт нажатия
            if (jumpDown) jumpWasDown.add(id); else jumpWasDown.remove(id);

            if (!justPressed) continue;
            if (player.onGround()) continue;                            // первый прыжок обычный
            if (airTicks.getOrDefault(id, 0) < MIN_AIR_TICKS) continue; // мини-отступ от прошлого прыжка
            if (player.getAbilities().flying) continue;

            // Сколько доп. прыжков даёт экипировка (атрибут air_jumps).
            AttributeInstance inst = player.getAttribute(ModAttributes.AIR_JUMPS);
            if (inst == null) continue;
            int maxJumps = (int) inst.getValue();
            int used = jumpsUsed.getOrDefault(id, 0);
            if (used >= maxJumps) continue;                             // лимит исчерпан

            Vec3 v = player.getDeltaMovement();
            player.setDeltaMovement(v.x, 0.55, v.z); // высоту подкрути под себя
            player.hurtMarked = true;                // форсим синк новой скорости на клиент
            jumpsUsed.put(id, used + 1);

            // Пример работы с DataComponent: пишем мутабельный счётчик прямо на стек ботинок.
            // Изменение синкается на клиент (стек надетой брони трекается) -> тултип обновляется.
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            int done = boots.getOrDefault(ModDataComponents.AIR_JUMP_COUNT.get(), 0);
            boots.set(ModDataComponents.AIR_JUMP_COUNT.get(), done + 1);
        }
    }
}

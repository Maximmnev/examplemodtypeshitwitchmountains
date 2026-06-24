package com.example.examplemod;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Хранит список чанков, в которых платформы уже сгенерированы.
 *
 * Это "SavedData" — игра САМА сохраняет его в файл <мир>/data/examplemod20_platforms.dat
 * при сохранении мира и САМА загружает обратно при заходе в мир. Ручной работы с файлами нет.
 * Хранилище своё у каждого измерения (overworld / nether / end).
 */
public class PlatformChunkData extends SavedData {

    // Каждый ChunkPos упакован в одно число long (так компактнее и проще сохранять).
    private final Set<Long> generated = new HashSet<>();

    // CODEC — это "переводчик" между нашим объектом и данными в файле сохранения.
    // В Minecraft нельзя просто записать Java-объект на диск: нужно описать, ИЗ ЧЕГО он состоит.
    // Codec знает оба направления:
    //   - при сохранении: берёт объект -> превращает в простые данные (числа/списки) -> игра пишет в .dat
    //   - при загрузке:   игра читает данные из .dat -> Codec собирает из них обратно наш объект
    //
    // Codec.LONG.listOf() = "список чисел long". То есть в файле будет просто список чисел.
    // .xmap(...) принимает две функции-переводчика: первая собирает объект ИЗ списка,
    // вторая разбирает объект В список.
    public static final Codec<PlatformChunkData> CODEC = Codec.LONG.listOf().xmap(
            list -> {                                   // из файла (список чисел) -> в наш объект
                PlatformChunkData data = new PlatformChunkData();
                data.generated.addAll(list);
                return data;
            },
            // из нашего объекта -> в список для файла.
            // ArrayList — это обычный список (массив, который умеет расти): хранит элементы по порядку.
            // Codec.LONG.listOf() ждёт именно List, а у нас Set, поэтому оборачиваем Set в новый ArrayList.
            data -> new ArrayList<>(data.generated)
    );

    // "Тип" говорит игре: как называется файл, как создать пустой объект, каким Codec'ом читать/писать.
    public static final SavedDataType<PlatformChunkData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(ExampleMod20.MODID, "platforms"),
            PlatformChunkData::new,
            CODEC
    );

    /** true, если в этом чанке платформа уже стоит. */
    public boolean isGenerated(ChunkPos pos) {
        return generated.contains(pos.pack());
    }

    /** Запоминаем, что чанк обработан, и помечаем данные "грязными" — чтобы игра их сохранила. */
    public void markGenerated(ChunkPos pos) {
        if (generated.add(pos.pack())) {
            setDirty();
        }
    }

    /** Берём хранилище нужного измерения (создаётся при первом обращении). */
    public static PlatformChunkData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}

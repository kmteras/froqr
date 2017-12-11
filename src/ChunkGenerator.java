import java.util.ArrayList;
import java.util.Random;

public class ChunkGenerator {
    private Random random;

    public ChunkGenerator(int seed) {
        random = new Random(seed);
    }

    public Chunk getNextChunk(long offset) {
        return new Chunk(offset - (long)Froqr.GAME_SIZE_Y * 1_000_000_000L, random.nextInt());
    }

    public ArrayList<Chunk> getStartingChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();

        for(int i = 0; i < 16; i++) {
            chunks.add(0, new Chunk((long)(i * Tile.TILESIZE) * 1_000_000_000, random.nextInt()));
        }
        return chunks;
    }
}

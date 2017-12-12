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
            if(i > 10) {
                //VERY inefficient way to get grass chunks to start on
                Chunk chunk;
                do {
                    chunk = new Chunk((long)(i * Tile.TILESIZE) * 1_000_000_000, random.nextInt());
                } while(chunk.getType() != ChunkType.GRASS);

                chunks.add(0, chunk);
            }
            else {
                chunks.add(0, new Chunk((long) (i * Tile.TILESIZE) * 1_000_000_000, random.nextInt()));
            }
        }
        return chunks;
    }
}

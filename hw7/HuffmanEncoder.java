import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();
        for (char chr : inputSymbols) {
            if (!map.containsKey(chr)) {
                map.put(chr, 0);
            }
            map.replace(chr, map.get(chr) + 1);
        }
        return map;
    }
    public static void main(String[] args) {
        String filename = args[0];
        char[] text = FileUtils.readFile(filename);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(text);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        ObjectWriter objectWriter = new ObjectWriter(args[0] + ".huf");
        objectWriter.writeObject(trie);
        objectWriter.writeObject(text.length);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> list = new ArrayList<>();
        for (int i = 0; i < text.length; ++i) {
            list.add(lookupTable.get(text[i]));
        }
        objectWriter.writeObject(BitSequence.assemble(list));
    }
}

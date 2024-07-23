public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader objectReader = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) objectReader.readObject();
        int number = (int) objectReader.readObject();
        BitSequence bitSequence = (BitSequence) objectReader.readObject();
        char[] chrs = new char[number];
        for (int i = 0; i < number; ++i) {
            Match m = trie.longestPrefixMatch(bitSequence);
            bitSequence = bitSequence.allButFirstNBits(m.getSequence().length());
            chrs[i] = m.getSymbol();
        }
        FileUtils.writeCharArray(args[1], chrs);
    }
}

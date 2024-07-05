public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new ArrayDeque<>();
        for (int i = 0; i < word.length(); ++i) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        boolean flag = true;
        while (deque.size() >= 2) {
            flag = (deque.removeFirst().equals(deque.removeLast())) && flag;
        }

        return flag;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        boolean flag = true;
        while (deque.size() >= 2) {
            flag = (cc.equalChars(deque.removeFirst(), deque.removeLast())) && flag;
        }

        return flag;
    }


}

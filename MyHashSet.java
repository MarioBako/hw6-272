import java.security.*;
import java.util.*;
import java.io.*;

class myHashSet {



    public ArrayList<String> listCreator(){
        ArrayList<String> words = new ArrayList<>();

        try{
            Scanner s = new Scanner(new File("/Users/mbakollari/IdeaProjects/hw6/src/EnglishWordList.txt"));
            while (s.hasNext()) {
                words.add(s.next());
            }
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        Collections.sort(words);

        for(int i = 0; i < words.size() - 1; i++){
            String current = words.get(i);
            String next = words.get(i + 1);
            if(current.compareTo(next) == 0 || next.length() == 0){
                words.remove(i + 1);
                i--;
            }
        }
        return words;
    }

    public int hashCode1(String s){
        return Math.abs(s.hashCode());
    }

    public int hashCode2(Object key){
        int h;

        return Math.abs((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16));
    }

    public BitSet hashCode3(String s){
        byte[] key = null;
        byte[] sb = s.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            key = md.digest(sb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return BitSet.valueOf(key);


    }


    public int H1p50(String s){
        return  hashCode1(s) % 524287;
    }

    public int H1p95(String s){
        return  hashCode1(s) % 262127;
    }

    public int H2p50(String s){
        return  hashCode2(s) % 524287;
    }

    public int H2p95(String s){
        return  hashCode2(s) % 262127;
    }

    public int H3p50(String s){
        BitSet bs = hashCode3(s);
        int[] ints = {2, 7,17,29,41,53,67,79,97,107,127,139,157,173,191, 199,227,239, 241};
        int value = 0;
        for(int i = 0; i < ints.length; i++){
            value *= 2;
            if(bs.get(ints[i])){
                value++;
            }
        }
        return  value;
    }


    public int H3p95(String s){
        BitSet bs = hashCode3(s);
        int[] ints = {2, 7,17,29,41,53,67,79,97,107,127,139,157,173,191, 199,227,239};
        int value = 0;
        for(int i = 0; i < ints.length; i++){
            value *= 2;
            if(bs.get(ints[i])){
                value++;
            }
        }
        return  value;
    }

    public ArrayList<LinkedList<String>> H(ArrayList<String> words, int method, boolean is95){
        ArrayList<LinkedList<String>> list = new ArrayList<>();
        int size = -1;

        switch(method){
            case 1:
            case 2:
                size = is95 ? 262127 : 524287;
                break;
            case 3:
                size = is95 ? 262144 : 524288;
                break;
        }

        for(int i = 0; i < size; i++){
            list.add(i, new LinkedList<>());
        }

        for(String word : words ){
            int hash = -1;
            switch(method){
                case 1:
                    hash = is95 ? H1p95(word) : H1p50(word);
                    break;
                case 2:
                    hash = is95 ? H2p95(word) : H2p50(word);
                    break;
                case 3:
                    hash = is95 ? H3p95(word) : H3p50(word);
                    break;
            }
            list.get(hash).add(word);
        }

        return list;
    }

    public double averageLength(ArrayList<LinkedList<String>> list){
        double sum = 0;
        int size = 0;

        for(int i = 0; i < list.size(); i++){
            sum += list.get(i).size();
            if(list.get(i).size() > 0){
                size++;
            }
        }

        return sum / size;
    }

    public int collisionCount(ArrayList<LinkedList<String>> list){
        int value = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).size() > 1){
                value += list.get(i).size() - 1;
            }
        }

        return value;
    }

    public static void main(String[] args){
        myHashSet test = new myHashSet();
        ArrayList<String> words = test.listCreator();

        double start1p95 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list1p95 = test.H(words, 1, true);
        double end1p95 = System.currentTimeMillis();
        double start1p50 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list1p50 = test.H(words, 1, false);
        double end1p50 = System.currentTimeMillis();
        double start2p95 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list2p95 = test.H(words, 2, true);
        double end2p95 = System.currentTimeMillis();
        double start2p50 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list2p50 = test.H(words, 2, false);
        double end2p50 = System.currentTimeMillis();
        double start3p95 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list3p95 = test.H(words, 3, true);
        double end3p95 = System.currentTimeMillis();
        double start3p50 = System.currentTimeMillis();
        ArrayList<LinkedList<String>> list3p50 = test.H(words, 3, false);
        double end3p50 = System.currentTimeMillis();

        System.out.println("Method 1 @ 95%: Milliseconds: " + (end1p95 - start1p95) + ", Average Length: "
                + test.averageLength(list1p95) + ", Collisions: " + test.collisionCount(list1p95));

        System.out.println("Method 1 @ 50%: Milliseconds: " + (end1p50 - start1p50) + ", Average Length: "
                + test.averageLength(list1p50) + ", Collisions: " + test.collisionCount(list1p50));

        System.out.println();

        System.out.println("Method 2 @ 95%: Milliseconds: " + (end2p95 - start2p95) + ", Average Length: "
                + test.averageLength(list2p95) + ", Collisions: " + test.collisionCount(list2p95));

        System.out.println("Method 2 @ 50%: Milliseconds: " + (end2p50 - start2p50) + ", Average Length: "
                + test.averageLength(list2p50) + ", Collisions: " + test.collisionCount(list2p50));

        System.out.println();

        System.out.println("Method 3 @ 95%: Milliseconds: " + (end3p95 - start3p95) + ", Average Length: "
                + test.averageLength(list3p95) + ", Collisions: " + test.collisionCount(list3p95));

        System.out.println("Method 3 @ 50%: Milliseconds: " + (end3p50 - start3p50) + ", Average Length: "
                + test.averageLength(list3p50) + ", Collisions: " + test.collisionCount(list3p50));
    }

}
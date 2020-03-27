package flashcards;
import java.io.*;
import java.util.*;

public class Flashcard {

    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> hard = new LinkedHashMap<>();
    private ArrayList<String> log = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);

    private void add() {
        String card = "The card:";
        System.out.println(card);
        log.add(card);
        String addF = scan.nextLine();
        log.add(addF);
        if (map.containsValue(addF)) {
            String fall = "The card \"" + addF + "\" already exists.\n";
            System.out.println(fall);
            log.add(fall);
            return;
        }
        String definition = "The definition of the card:";
        System.out.println(definition);
        log.add(definition);
        String addS = scan.nextLine();
        log.add(addS);
        if (map.containsKey(addS)) {
            String fall = "The definition \"" + addS + "\" already exists.\n";
            System.out.println(fall);
            log.add(fall);
            return;
        }
        String complete = "The (\"" + addF + "\":\"" + addS + "\") has been added.\n";
        System.out.println(complete);
        log.add(complete);
        map.put(addS, addF);
        hard.put(addF, 0);
    }

    private void remove() {
        String card = "The card:";
        System.out.println(card);
        log.add(card);
        String remove = scan.nextLine();
        log.add(remove);
        if (!map.containsValue(remove)) {
            String fall = "Can't remove \"" + remove + "\": there is no such card.\n";
            System.out.println(fall);
            log.add(fall);
            return;
        }
        String rem = "";
        for (String key : map.keySet()) {
            if (map.get(key).equals(remove)) {
                rem = key;
                break;
            }
        }
        map.remove(rem);
        hard.remove(remove);
        String complete = "The card has been removed.";
        System.out.println(complete);
        log.add(complete);
    }

    private void importCards(String fileName) {
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            Scanner fileScan = new Scanner(fis);
            int added = 0;
            while (fileScan.hasNextLine()) {
                String value = fileScan.nextLine();
                String key = fileScan.nextLine();
                int errors = Integer.parseInt(fileScan.nextLine());
                if (map.containsValue(value)) {
                    String rem = "";
                    for (String keys : map.keySet()) {
                        if (map.get(keys).equals(value)) {
                            rem = keys;
                            break;
                        }
                    }
                    map.remove(rem);
                    hard.remove(value);
                } else if (map.containsKey(key)) {
                    hard.remove(map.get(key));
                    map.remove(key);
                }
                map.put(key, value);
                hard.put(value, errors);
                added++;
            }
            String complete = added + " cards have been loaded.\n";
            System.out.println(complete);
            log.add(complete);
        } catch (Exception e) {
            String fall = "File not found\n";
            System.out.println(fall);
            log.add(fall);
        }

    }

    private void export(String fileName) {
        try (FileWriter fw = new FileWriter(fileName)) {
            int saved = 0;
            for (String key : map.keySet()) {
                fw.write(map.get(key) + "\n");
                fw.write(key + "\n");
                fw.write(hard.get(map.get(key)) + "\n");
                saved++;
            }
            String complete = saved + " cards have been saved.\n";
            System.out.println(complete);
            log.add(complete);
        } catch (Exception e) {

        }
    }

    private void ask() {
        String times = "How many times to ask?";
        System.out.println(times);
        log.add(times);
        int ask = Integer.parseInt(scan.nextLine());
        log.add(ask + "");
        Random ran = new Random();
        String[][] strings = new String[map.size()][2];
        int b = 0;
        for (String key : map.keySet()) {
            strings[b][0] = map.get(key);
            strings[b][1] = key;
            b++;
        }
        for (int i = 0; i < ask; i++) {
            int num = ran.nextInt(strings.length);
            String answer = strings[num][0];
            String first = "Print the definition of the \"" + answer + "\":";
            System.out.println(first);
            log.add(first);
            String ans = scan.nextLine();
            log.add(ans);
            if (map.getOrDefault(ans, "").equals(answer)) {
                String correct = "Correct answer.";
                System.out.println(correct);
                log.add(correct);
            } else {
                hard.put(answer, hard.get(answer) + 1);
                String wrong = "Wrong answer. The correct one is \"" + strings[num][1] + "\"";
                System.out.print(wrong);
                if (map.containsKey(ans)) {
                    String wg = ", you've just written the definition of \"" + map.get(ans) + "\".";
                    System.out.println(wg);
                    log.add(wrong + wg);
                } else {
                    String wg = ".";
                    System.out.println(wg);
                    log.add(wrong + wg);
                }
            }
        }
        System.out.println();
        log.add("");
    }

    private void log() {
        String fileN = "File name:";
        System.out.println(fileN);
        log.add(fileN);
        String logFile = scan.nextLine();
        log.add(logFile);
        String message = "The log has been saved.";
        try (FileWriter fw = new FileWriter(logFile)){
            for (String data : log) {
                fw.write(data + "\n");
            }
        } catch (Exception e) {}
        System.out.println(message);
    }

    private void hardCard() {
        ArrayList<String> errors = new ArrayList<>();
        int maxError = 1;
        for (String card : hard.keySet()) {
            if (hard.get(card) == maxError) {
                errors.add("\"" + card + "\"");
            } else if (hard.get(card) > maxError) {
                errors.clear();
                maxError = hard.get(card);
                errors.add("\"" + card + "\"");
            }
        }
        if (errors.isEmpty()) {
            String empty = "There are no cards with errors.\n";
            System.out.println(empty);
            log.add(empty);
        } else if (errors.size() == 1) {
            String error = "The hardest card is " + errors.get(0) + ". You have " + maxError + " errors answering it.\n";
            System.out.println(error);
            log.add(error);
        } else {
            String error = "The hardest cards are ";
            for (int i = 0; i < errors.size(); i++) {
                if (i < errors.size() - 1) {
                    error += errors.get(i) + ", ";
                } else {
                    error += errors.get(i) + ". You have " + maxError + " errors answering them.\n";
                }
            }
            System.out.println(error);
            log.add(error);
        }
    }

    private void reset() {
        hard.replaceAll((c, v) -> 0);
        String reset = "Cards statistics has been reset.\n";
        System.out.println(reset);
        log.add(reset);
    }

    private String getArg(String[] args, String b, String defaultValue) {
        for (int i = 0; i < args.length-1; i += 2) {
            if (args[i].equals(b)) {
                return args[++i];
            }
        }
        return defaultValue;
    }

    public void start(String[] args) {
        String imp = getArg(args, "-import", "");
        if(!imp.equals("")) {
            importCards(imp);
        }
        boolean b = true;
        do {
            String out = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            System.out.println(out);
            log.add(out);
            String choice = scan.nextLine();
            log.add(choice);
            switch (choice) {
                case "add":
                    this.add();
                    break;
                case "remove":
                    this.remove();
                    break;
                case "import":
                    String fileN = "File name:";
                    System.out.println(fileN);
                    log.add(fileN);
                    String fileName = scan.nextLine();
                    log.add(fileName);
                    this.importCards(fileName);
                    break;
                case "export":
                    String fileNa = "File name:";
                    System.out.println(fileNa);
                    log.add(fileNa);
                    String export = scan.nextLine();
                    log.add(export);
                    this.export(export);
                    break;
                case "ask":
                    this.ask();
                    break;
                case "log":
                    this.log();
                    break;
                case "hardest card":
                    this.hardCard();
                    break;
                case "reset stats":
                    this.reset();
                    break;
                case "exit":
                    b = false;
                    break;
            }
        } while (b);
        System.out.println("Bye bye!");
        String exp = getArg(args, "-export", "");
        if(!exp.equals("")) {
            export(exp);
        }
    }
}

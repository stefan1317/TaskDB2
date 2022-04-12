package Main.businesslayer;

import Main.user.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServices {

    public long mailService(List<String> list) {
        return list.stream().filter(s -> s.contains("@gmail")).count();
    }

    public Set<String> lastNameService(List<String> list) {
        return list.stream().collect(Collectors.toSet());
    }

    public String getStringOfInitialsService(List<String> list) {
        List<Character> characters = list.stream().map(s -> s.charAt(0)).collect(Collectors.toList());

        String s = String.valueOf(characters.get(0));

        for(int i = 1; i < characters.size(); i++) {
            s = s.concat(String.valueOf(characters.get(i)));
        }

        return s;
    }

    public long getNumberOfUsersUnder20(List<User> list) {
        return list.stream().filter(u -> u.getAge() < 20).
                filter(u -> u.getFirstName().contains("a") || u.getFirstName().contains("A")).count();
    }

    public List<String> getInitialsString(List<String> firstNameList, List<String> lastNameList) {
        List<Character> firstNameChars = firstNameList.stream().map(s -> s.charAt(0)).collect(Collectors.toList());
        List<Character> lastNameChars = lastNameList.stream().map(s -> s.charAt(0)).collect(Collectors.toList());

        List<String> finalList = new ArrayList<>();

        for (int i = 0; i < lastNameChars.size(); i++) {
            finalList.add(i, String.valueOf(firstNameChars.get(i)).concat(String.valueOf(lastNameChars.get(i))));
        }

        return finalList;
    }
}

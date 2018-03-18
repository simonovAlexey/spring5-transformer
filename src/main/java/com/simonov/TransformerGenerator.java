package com.simonov;

import com.simonov.transformer.data.Activity;
import com.simonov.transformer.data.Transformer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TransformerGenerator
{
    private static final Random RND = new Random();

    private static int counter = 0;
    private static String names[] = {"BB", "Optimus", "Braints", "Galvatron"};

    private static Activity[] activities = new Activity[]
            {
                    new Activity("Say"),
                    new Activity("Kill"),
                    new Activity("Transform")
            };

    private static String[] actions =
            {
                    "Тяжело без башки выпендриваться, а?",
                    "Крошка кролик-энерджайзер прямиком из ада!",
                    "У меня есть небольшой сюрприз для тебя, сынок.",
                    "Через 50 лет ты будешь жалеть о том, что не хватило смелости сесть в машину…",
                    "Не водитель выбирает машину, а машина водителя",
                    "Глупые насекомые пытались подстрелить меня!",
                    "Судя по составу феромонов ты хочешь совокупиться с этой самкой."
            };

    public static Transformer generateTransformer()
    {
        Transformer transformer = new Transformer();

        transformer.setName(names[counter++]);
        transformer.setAge((counter + transformer.getName().length())*1000);

        transformer.setActivity(getActivityList());

        return transformer;
    }

    public static Transformer generateTransformerWithIdAndName(long id, String name)
    {
        Transformer transformer = new Transformer();

        transformer.setId(id);
        transformer.setName(name);
        transformer.setAge((counter + transformer.getName().length())*1000);

        transformer.setActivity(getActivityList());
        return transformer;
    }

    private static List<Activity> getActivityList()
    {
        return Arrays.asList(activities);
    }

    public static String getRandomAction()
    {
        return actions[RND.nextInt(actions.length - 1)];
    }

}

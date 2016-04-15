package jiraiyah.librarian.infrastructure;

import java.util.LinkedList;

public enum ReviveChange
{
    PlayerRevive,
    PlayerDevive,
    ModRevive,
    DimensionRevive;

    public LinkedList<Object> list;

    public static void load()
    {
        for (ReviveChange change : values())
            change.list = new LinkedList<Object>();
    }
}

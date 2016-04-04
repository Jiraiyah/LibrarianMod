package jiraiyah.librarian.references;

public enum VillagerProfessions
{
    farmer(0, 1),  fisherman(0, 2),  shepherd(0, 3),  fletcher(0, 4),  librarian(1, 0),  cleric(2, 0),  armor(3, 1),  weapon(3, 2),  tool(3, 3),  butcher(4, 1),  leather(4, 2);

    public final int profession;
    public final int career;

    private VillagerProfessions(int profession, int career)
    {
        this.profession = profession;
        this.career = career;
    }
}

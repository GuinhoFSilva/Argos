package guinho.olympus.core.domain.player.enums;

public enum Rank {
    BRONZE(1),
    SILVER(2),
    GOLD(3);

    private final int tier;

    Rank(int tier) {
        this.tier = tier;
    }
}

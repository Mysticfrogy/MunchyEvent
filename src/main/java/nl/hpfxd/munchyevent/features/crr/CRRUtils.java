package nl.hpfxd.munchyevent.features.crr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CRRUtils {
    public static List<CRRPlayer> sortByKills(List<CRRPlayer> players) {
        List<CRRPlayer> result = new ArrayList<>(players);
        result.sort(Comparator.comparing(CRRPlayer::getFightsWon));
        return result;
    }
}

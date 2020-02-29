package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.RankDTO;

public interface RankPresenterCallBack {
    void rankIsLoaded(RankDTO rankDTO);
    void ranksAreLoaded(List<RankDTO> ranks);
}

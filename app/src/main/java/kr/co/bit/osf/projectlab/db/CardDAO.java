package kr.co.bit.osf.projectlab.db;

import java.util.List;

public interface CardDAO {
    boolean addCard(CardDTO card);
    CardDTO getCard(int id);
    boolean deleteCard(int id);
    boolean updateCard(CardDTO newValue);
    List<CardDTO> getCardByBoxId(int boxId);
    boolean deleteCardByBoxId(int boxId);
}

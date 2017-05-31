package com.sinoautodiagnoseos.entity.CarBrands;

/**
 * Created by dingxujun on 2017/5/23.
 */

public class Alphabet {
    private String letter;//字母
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alphabet alphabet = (Alphabet) o;

        return letter.equals(alphabet.letter);

    }

    @Override
    public int hashCode() {
        return letter.hashCode();
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }


}

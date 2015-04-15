package utils
/**
 * Helper class
 */
class TwoTuple {
    private def first;
    private def second;

    TwoTuple(def first, def second) {
        this.first = first;
        this.second = second;
    }

    def getFirst() {
        return first;
    }

    def getSecond() {
        return second;
    }

}

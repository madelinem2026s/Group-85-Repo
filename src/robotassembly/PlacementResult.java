package robotassembly;

/** Results returned by model actions; UI decides how to display them. */
public enum PlacementResult {
    CORRECT, CORRECT_DISCARD, WRONG, WRONG_DISCARD,
    OCCUPIED, INVALID_SPOT, GAME_OVER
}

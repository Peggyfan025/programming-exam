package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        PieceMoveCalc cal;
        if (piece.getPieceType()==PieceType.BISHOP){
            cal = new BishopMove(board,myPosition,piece);
        }
        else if (piece.getPieceType()==PieceType.ROOK){
            cal = new RookMove(board,myPosition,piece);
        }
        else if (piece.getPieceType()==PieceType.QUEEN){
            cal = new QueenMove(board,myPosition,piece);
        }
        else if (piece.getPieceType()==PieceType.KNIGHT){
            cal = new KnightMove(board,myPosition,piece);
        }
        else{
            return List.of();
        }
        return cal.pieceMove();
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
abstract class PieceMoveCalc{
    protected ChessBoard board;
    protected ChessPosition position;
    protected ChessPiece piece;

    public PieceMoveCalc(ChessBoard board, ChessPosition position, ChessPiece piece){
        this.board = board;
        this.position = position;
        this.piece = piece;
    }
    public abstract Collection<ChessMove> pieceMove();
    protected static final int[][] STRAIGHT_DIRECTION = {{0,1}, {1, 0}, {0,-1},{-1,0}};
    protected static final int[][] DIAGONAL_DIRECTION = {{1,1}, {1, -1}, {-1,-1},{-1,1}};

    protected boolean inRange(int row, int col){
        return row<=8 && row>=1 && col>=1 && col<=8;
    }

    protected void addMove(Collection<ChessMove> moves, int[][] directions){
        int row = position.getRow();
        int col = position.getColumn();
        for (int [] direction:directions){
            int r = row + direction[0];
            int c = col + direction[1];
            while (inRange(r,c)){
                ChessPosition end = new ChessPosition(r,c);
                if (board.getPiece(end) == null){
                    moves.add(new ChessMove(position, end, null));
                }
                else if (board.getPiece(end).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(position, end, null));
                    break;
                }
                else {
                    break;
                }
                r+=direction[0];
                c+=direction[1];
            }
        }
    }
}
class BishopMove extends PieceMoveCalc{
    public BishopMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();
        addMove(moves,DIAGONAL_DIRECTION);
        return moves;
    }
}
class RookMove extends PieceMoveCalc{
    public RookMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();
        addMove(moves,STRAIGHT_DIRECTION);
        return moves;
    }
}
class QueenMove extends PieceMoveCalc{
    public QueenMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();
        addMove(moves,DIAGONAL_DIRECTION);
        addMove(moves,STRAIGHT_DIRECTION);
        return moves;
    }
}
class KnightMove extends PieceMoveCalc{
    public KnightMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    int[][] knight_direction = {{2,1}, {-2, 1}, {-2, -1}, {2,-1}, {1, 2}, {1, -2},{-1,2},{-1,-2}};
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        for (int[] direction:knight_direction){
            int r = row + direction[0];
            int c = col + direction[1];

            if (inRange(r,c)){
                ChessPosition end = new ChessPosition(r,c);
                if (board.getPiece(end) == null){
                    moves.add(new ChessMove(position,end,null));
                }
                else if (board.getPiece(end).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(position,end,null));
                }
            }
        }
        return moves;
    }
}
class KingMove extends PieceMoveCalc{
    public KingMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();

        return moves;
    }
}
class PawnMove extends PieceMoveCalc{
    public PawnMove(ChessBoard board, ChessPosition position, ChessPiece piece){
        super(board,position,piece);
    }
    @Override
    public Collection<ChessMove> pieceMove(){
        Collection<ChessMove> moves = new ArrayList<>();

        return moves;
    }
}

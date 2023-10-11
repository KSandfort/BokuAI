package agent.utils;

import engine.BoardState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TreeNode {
    private TreeNode parentNode;
    private ArrayList<TreeNode> children;
    private BoardState state;
    private int score; //Maybe change to different data type?

}

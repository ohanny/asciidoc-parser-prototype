package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ToStringAnalysisBuilder implements ParsingProcessListener {

    private Deque<TreeNode> stack = new LinkedList<>();

    class TreeNode {
        String label;
        boolean match;
        int level;
        StringBuilder chars;
        int position;
        List<TreeNode> children = new LinkedList<>();

        void add(TreeNode child) {
            children.add(child);
        }

        void addChar(char c) {
            if (chars == null) {
                chars = new StringBuilder();
            }
            chars.append(c);
        }

    }

    private void print(TreeNode node) {

        System.out.println(getTab(node.level) + node.label +
                " (" + node.position + ")" +
                (node.chars == null?"":" - chars read -> [" + node.chars + "]") +
                (node.match?(node.level == 0?" - MATCHED":""):" - FAILED"));
        node.children.forEach(this::print);

    }

    private String getTab(int level) {
        String tab = "";
        for (int i = 0; i < level; i++) {
            tab += "  ";
        }
        return tab;
    }

    @Override
    public void matcherStart(Matcher matcher, int level, int position) {
        TreeNode node = new TreeNode();
        node.label = matcher.getLabel();
        node.level = level;
        node.position = position;
        if (level > 0) {
            TreeNode parent = stack.peek();
            parent.add(node);
        }

        stack.push(node);
    }

    @Override
    public void nextChar(char c) {
        TreeNode node = stack.peek();
        node.addChar(c);
    }

    @Override
    public void matcherEnd(Matcher matcher, boolean match) {
        TreeNode node = stack.pop();
        node.match = match;

        if (node.level == 0) {
            print(node);
        }
    }
}

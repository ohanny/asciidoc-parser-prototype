package fr.icodem.asciidoc.parser2;

public class ParseTree {
//    Tree getParent();
//
//    Object getPayload();
//
//    Tree getChild(int var1);
//
//    int getChildCount();
//
//    String toStringTree();


    public ParseTree parent;
//    public RuleContext parent;
//    public int invokingState = -1;

//    public RuleContext() {
//    }
//
//    public RuleContext(RuleContext parent, int invokingState) {
//        this.parent = parent;
//        this.invokingState = invokingState;
//    }
//
//    public int depth() {
//        int n = 0;
//
//        for(RuleContext p = this; p != null; ++n) {
//            p = p.parent;
//        }
//
//        return n;
//    }

//    public boolean isEmpty() {
//        return this.invokingState == -1;
//    }

//    public Interval getSourceInterval() {
//        return Interval.INVALID;
//    }

//    public RuleContext getRuleContext() {
//        return this;
//    }

//    public RuleContext getParent() {
//        return this.parent;
//    }

//    public RuleContext getPayload() {
//        return this;
//    }

//    public String getText() {
//        if(this.getChildCount() == 0) {
//            return "";
//        } else {
//            StringBuilder builder = new StringBuilder();
//
//            for(int i = 0; i < this.getChildCount(); ++i) {
//                builder.append(this.getChild(i).getText());
//            }
//
//            return builder.toString();
//        }
//    }

//    public int getRuleIndex() {
//        return -1;
//    }

//    public ParseTree getChild(int i) {
//        return null;
//    }

//    public int getChildCount() {
//        return 0;
//    }

//    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
//        return visitor.visitChildren(this);
//    }

//    public String toStringTree(org.antlr.v4.runtime.Parser recog) {
//        return Trees.toStringTree(this, recog);
//    }

//    public String toStringTree(List<String> ruleNames) {
//        return Trees.toStringTree(this, ruleNames);
//    }

//    public String toStringTree() {
//        return this.toStringTree((List)null);
//    }

//    public String toString() {
//        return this.toString((List)null, (RuleContext)null);
//    }

//    public final String toString(Recognizer<?, ?> recog) {
//        return this.toString((Recognizer)recog, ParserRuleContext.EMPTY);
//    }

//    public final String toString(List<String> ruleNames) {
//        return this.toString((List)ruleNames, (RuleContext)null);
//    }

//    public String toString(Recognizer<?, ?> recog, RuleContext stop) {
//        String[] ruleNames = recog != null?recog.getRuleNames():null;
//        List ruleNamesList = ruleNames != null? Arrays.asList(ruleNames):null;
//        return this.toString(ruleNamesList, stop);
//    }
//
//    public String toString(List<String> ruleNames, RuleContext stop) {
//        StringBuilder buf = new StringBuilder();
//        RuleContext p = this;
//        buf.append("[");
//
//        for(; p != null && p != stop; p = p.parent) {
//            if(ruleNames == null) {
//                if(!p.isEmpty()) {
//                    buf.append(p.invokingState);
//                }
//            } else {
//                int ruleIndex = p.getRuleIndex();
//                String ruleName = ruleIndex >= 0 && ruleIndex < ruleNames.size()?(String)ruleNames.get(ruleIndex):Integer.toString(ruleIndex);
//                buf.append(ruleName);
//            }
//
//            if(p.parent != null && (ruleNames != null || !p.parent.isEmpty())) {
//                buf.append(" ");
//            }
//        }
//
//        buf.append("]");
//        return buf.toString();
//    }

}

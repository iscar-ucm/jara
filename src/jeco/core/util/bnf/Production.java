/*
 * Copyright (C) 2010 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package jeco.core.util.bnf;

import java.util.ArrayList;

/**
 * Represents a production in a BNF grammar.
 * A production is a list of symbols.
 * The production can be recursive and have a minimum depth.
 * The minimum depth is the minimum number of non-terminal symbols that must be expanded to reach a terminal symbol
 */
public class Production extends ArrayList<Symbol> {

	private static final long serialVersionUID = 1L;
	// Variables
    /**
     * Recursive nature of production
     */
    protected boolean recursive; // Recursive nature of production
    /**
     * Minimum depth of parse tree for production to map to terminal symbol(s)
     */
    protected int minimumDepth; // Minimum depth of parse tree for production to map to terminal symbol(s)
    
    /*public Production(int newLength){
        super(newLength);
        setRecursive(false);
        setMinimumDepth(Integer.MAX_VALUE>>1);
    }*/
    
    /**
     * Default constructor
     */
    public Production(){
        super();
    }

    @Override
    public Production clone() {
    	Production clone = new Production();
    	for(Symbol symbol : this) {
    		clone.add(symbol.clone());
    	}
    	clone.recursive = recursive;
    	clone.minimumDepth = minimumDepth;
    	return clone;
    }
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for(int i = 0;i<this.size();i++) {
        	Symbol symbol = this.get(i);
            buffer.append(symbol.symbolString);
        }
        return buffer.toString();
    }
    
/*
     public Production(Production copy){
 
        super(copy);
        this.recursive = copy.recursive;
        this.minimumDepth = copy.minimumDepth;
    }*/
    
/*    public boolean getRecursive() {
        return recursive;
    }
    
    public void setRecursive(boolean newRecursive){
        recursive = newRecursive;
    }*/
    
    /*
     
    public int getMinimumDepth() {    
        return minimumDepth;
    }*/
    
    /*public void setMinimumDepth(int newMinimumDepth){
        minimumDepth = newMinimumDepth;
    }*/
    
    /*public int getNTSymbols() {
        int cnt = 0;
        for (Symbol o : this) {
            if (o.type == Symbol.SYMBOL_TYPE.NT_SYMBOL) {
                cnt++;
            }
        }
        return cnt;
    }*/
    
    /*@Override
    @SuppressWarnings({"ForLoopReplaceableByForEach"})
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0;i<this.size();i++) {
            s.append(this.get(i).getSymbolString());
        }
        return s.toString();
    }*/
}
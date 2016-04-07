package com.edu.neu.coe.analysis.clean;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Postag {
	
	public Postag(){}
	
	public static String getTag(String text){
		MaxentTagger tagger = new MaxentTagger("/Users/YuanHank/Downloads/stanford-postagger-2015-12-09/models/english-bidirectional-distsim.tagger");
		return tagger.tagString(text);
	}
	
	public static void main(String[] args){
		System.out.println(getTag("excellent food. Especially the fish is very good "));
	}
}

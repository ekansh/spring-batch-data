package com.example.batchprocessing.stepsconfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;

import com.example.batchprocessing.constants.Constants;

public class CustomLineTokenizer extends AbstractLineTokenizer{
	@Override
	protected List<String> doTokenize(String line) {
		List<String> l   = new ArrayList<>();
		String[] split = line.split(",");
		for (int i = 0; i < split.length; i++) {
			String string = split[i];
			if( Constants.CANCEL.equalsIgnoreCase(split[0]) && i==Constants.NAME_INDEX) {
				l.add("");
			}
			l.add(string);
		}
		return l;
	}
}

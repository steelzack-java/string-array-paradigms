package com.steelzack.string.paradigm.original.expression;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;

import java.util.List;

import org.junit.Test;

public class SpellingCheckImplTest {

	private static final String _1 = "1'";
	private static final String _2 = "2'";
	private static final String _3 = ",3";
	private static final String ALGARVE = "Algarve";
	private static final String EXTREMADURA = "tr�s Os Montes E Alto Douro, , extremadura";
	private static final String RIBATEJO = "  Ribatejo";
	private static final String MINHO = "minho";

	@Test
	public void testCheckSpelling() throws Exception {
		String[] regions = { ALGARVE, MINHO, RIBATEJO, EXTREMADURA };
		String[] assignedNumbers = { _1, _2, _3 };
		SpellingCheckImpl check = new SpellingCheckImpl();
		for (String region : regions) {
			check.checkSpelling(region);
		}
		for (String number : assignedNumbers) {
			check.checkSpelling(number);
		}

		final List<String> resultSpellingBeginCapitalRegions = check.getSpellingBeginCapitalRegions();
		assertThat(resultSpellingBeginCapitalRegions, hasSize(2));
		for (String stringResult : resultSpellingBeginCapitalRegions) {
			assertThat(stringResult, anyOf(startsWith(ALGARVE), startsWith(RIBATEJO)));
		}

		final List<String> resultSpellingDoubleCommaRegions = check.getSpellingDoubleCommaRegions();
		assertThat(resultSpellingDoubleCommaRegions, hasSize(1));
		for (String stringResult : resultSpellingDoubleCommaRegions) {
			assertThat(stringResult, startsWith(EXTREMADURA));
		}

		final List<String> resultSpellingWhiteSpacesRegions = check.getSpellingWhiteSpacesRegions();
		assertThat(resultSpellingWhiteSpacesRegions, hasSize(1));
		for (String stringResult : resultSpellingWhiteSpacesRegions) {
			assertThat(stringResult, startsWith(RIBATEJO));
		}
	}
}
package com.github.tomakehurst.wiremock.matching.optional;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.MatchesJsonPathPattern;
import com.github.tomakehurst.wiremock.matching.MatchesXPathPattern;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.Map;

public class OptionalMatchesJsonPathPattern extends OptionalPattern {

    public OptionalMatchesJsonPathPattern(@JsonProperty("matchesOrAbsentJsonPath") final String expectedValue) {
        super(new MatchesJsonPathPattern(expectedValue));
    }

    public String getMatchesOrAbsentJsonPath() {
        return expectedValue;
    }

    @Override
    public MatchResult match(final String value) {
        final MatchResult match = super.match(value);
        final boolean matchOrNullJson = match.isExactMatch();

        return matchOrNullJson ? match : MatchResult.of(isJsonPathAbsent(value));
    }

    private boolean isJsonPathAbsent(final String value) {
        Object jsonPathObject = null;
        try {
            jsonPathObject = JsonPath.read(value, expectedValue);
        } catch (PathNotFoundException exception) {
            return true;
        }

        return jsonPathObject == null;
    }
}

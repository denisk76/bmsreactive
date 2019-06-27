package ru.bms.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public class IsEqualJSON extends DiagnosingMatcher<Object> {

    private final String expectedJSON;
    private JSONCompareMode jsonCompareMode;

    public IsEqualJSON(String expectedJSON) {
        this.expectedJSON = expectedJSON;
        this.jsonCompareMode = JSONCompareMode.STRICT;
    }

    private static String toJSONString(final Object o) {
        try {
            return o instanceof String ?
                    (String) o : new ObjectMapper().writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static IsEqualJSON equalToJson(final String expectedJSON) {
        return new IsEqualJSON(expectedJSON);
    }

    @Override
    protected boolean matches(Object actual, Description mismatchDescription) {
        String actualJSON = toJSONString(actual);
        try {
            JSONCompareResult result = JSONCompare.compareJSON(expectedJSON, actualJSON, jsonCompareMode);
            if (!result.passed()) {
                mismatchDescription.appendText(result.getMessage());
            }
            return result.passed();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expectedJSON);
    }
}

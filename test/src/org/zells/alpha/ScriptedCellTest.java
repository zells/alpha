package org.zells.alpha;

import org.junit.Assert;
import org.junit.Test;
import org.zells.alpha.dynamic.Cell;
import org.zells.alpha.dynamic.ScriptedCell;
import org.zells.alpha.dynamic.ScriptedResponse;

import java.util.HashMap;
import java.util.Map;

public class ScriptedCellTest {

    private Map<String, Cell> cells = new HashMap<String, Cell>();
    private Object response;

    @Test
    public void staticResponse() {
        givenACell_Responding("cell", "'Foo!'");
        whenISendAMessageTo("cell");
        thenTheResponseShouldBe("Foo!");
    }

    @Test
    public void respondToMessage() {
        givenACell_Responding("cell", "'Hello ' + message");
        whenISend_To("There", "cell");
        thenTheResponseShouldBe("Hello There");
    }

    @Test
    public void referenceSelf() {
        givenACell_Responding("cell", "self");
        whenISendAMessageTo("cell");
        thenTheResponseShouldBeTheCell("cell");
    }

    @Test
    public void accessChild() {
        givenACell_Responding("parent", "self.foo");
        given_HasTheChild("parent", "foo");

        whenISendAMessageTo("parent");
        thenTheResponseShouldBeTheCell("foo");
    }

    @Test
    public void createChildWithAssignment() {
        givenACell_Responding("cell", "self.bar = self.foo; self.bar");
        given_HasTheChild("cell", "foo");
        whenISendAMessageTo("cell");
        thenTheResponseShouldHaveTheStem("foo");
        then_ShouldHaveAChild("cell", "bar");
    }

    @Test
    public void accessParent() {
        givenACell("foo");
        given_HasTheChild("foo", "bar");
        given_RespondsWith("bar", "parent");

        whenISendAMessageTo("bar");
        thenTheResponseShouldBeTheCell("foo");
    }

    @Test
    public void sendMessage() {
        givenACell_Responding("cell", "self.foo('World')");
        given_HasTheChild("cell", "foo");
        given_RespondsWith("foo", "'Hello ' + message");

        whenISendAMessageTo("cell");
        thenTheResponseShouldBe("Hello World");
    }

    @Test
    public void noResponse() {
        givenACell("cell");
        whenISendAMessageTo("cell");
        thenTheResponseShouldBe(null);
    }

    @Test
    public void inheritResponse() {
        givenACell_Responding("stem", "'Hello ' + message");
        givenACell_Extending("cell", "stem");
        whenISend_To("World", "cell");
        thenTheResponseShouldBe("Hello World");
    }

    @Test
    public void keepContextInInheritedResponse() {
        givenACell_Responding("stem", "self");
        givenACell_Extending("cell", "stem");

        whenISendAMessageTo("cell");
        thenTheResponseShouldBeTheCell("cell");
    }

    private void givenACell(String name) {
        cells.put(name, new Cell(name, null, null));
    }

    private void givenACell_Extending(String name, String stem) {
        cells.put(name, new Cell(name, cells.get(stem), null));
    }

    private void given_RespondsWith(String name, String code) {
        cells.get(name).respond(new ScriptedResponse(code));
    }

    private void given_HasTheChild(String parentName, String childName) {
        Cell child = new Cell(childName, null, cells.get(parentName));
        cells.get(parentName).add(child);
        cells.put(childName, child);
    }

    private void givenACell_Responding(String name, String code) {
        givenACell(name);
        given_RespondsWith(name, code);
    }

    private void whenISendAMessageTo(String name) {
        whenISend_To(null, name);
    }

    private void whenISend_To(Object message, String cell) {
        response = new ScriptedCell(cells.get(cell)).call(message);
    }

    private void thenTheResponseShouldBeTheCell(String name) {
        Assert.assertEquals(cells.get(name), ((ScriptedCell) response).cell);
    }

    private void thenTheResponseShouldHaveTheStem(String cell) {
        Assert.assertEquals(cells.get(cell), ((ScriptedCell) response).cell.stem());
    }

    private void thenTheResponseShouldBe(Object o) {
        Assert.assertEquals(o, response);
    }

    private void then_ShouldHaveAChild(String parent, String child) {
        cells.get(parent).child(child);
    }
}

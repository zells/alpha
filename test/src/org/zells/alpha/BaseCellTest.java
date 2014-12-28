package org.zells.alpha;

import org.junit.Assert;
import org.junit.Test;
import org.zells.alpha.dynamic.Cell;

import java.util.*;

public class BaseCellTest {

    private Map<String, Cell> cells = new HashMap<String, Cell>();
    private Cell got;
    private Exception caught;

    @Test
    public void addChild() {
        givenACell("parent");
        given_HasAChild("parent", "foo");
        whenIGet("parent.foo");
        thenIShouldGet("foo");
    }

    @Test
    public void nonExistingChild() {
        givenACell("cell");
        whenTryToIGet("cell.foo");
        thenItShouldThrowTheException("Could not find [foo] in [cell]");
    }

    @Test
    public void removeChild() {
        givenACell("parent");
        given_HasAChild("parent", "foo");
        then_ShouldHave_Children("parent", 1);

        whenIRemove_From("foo", "parent");
        then_ShouldHave_Children("parent", 0);
    }

    @Test
    public void inheritChild() {
        givenACell("stem");
        given_HasAChild("stem", "foo");
        givenACell_Extending("cell", "stem");
        whenIGet("cell.foo");
        thenItsFullNameShouldBe("cell.foo");
    }

    @Test
    public void adoptChild() {
        givenACell("stem");
        given_HasAChild("stem", "foo");
        givenACell_Extending("cell", "stem");

        whenIAdd_To("bar", "cell.foo");

        then_ShouldHave_Children("foo", 0);
        then_ShouldHave_Children("cell.foo", 1);
        then_ShouldHaveTheStem("cell.foo", "foo");
    }

    @Test
    public void recursiveInheritanceAndAdoption() {
        givenACell("stem");
        givenACell_Extending("cell", "stem");
        given_HasAChild("stem", "foo");
        given_HasAChild("stem.foo", "bar");

        whenIGet("cell.foo.bar");
        thenItsFullNameShouldBe("cell.foo.bar");

        whenIAdd_To("baz", "cell.foo.bar");
        then_ShouldHaveTheStem("cell.foo.bar", "bar");
        then_ShouldHaveTheStem("cell.foo", "foo");
    }

    private void then_ShouldHaveTheStem(String cell, String stem) {
        Assert.assertEquals(cells.get(stem), resolve(cell).stem());
    }

    private Cell resolve(String path) {
        Stack<String> names = new Stack<String>();
        List<String> list = Arrays.asList(path.split("\\."));
        Collections.reverse(list);
        names.addAll(list);

        Cell cell = cells.get(names.pop());
        while (!names.isEmpty()) {
            cell = cell.child(names.pop());
        }
        return cell;
    }

    private void whenIAdd_To(String child, String cell) {
        resolve(cell).add(new Cell(child, null, null));
    }

    private void thenIShouldGet(String cell) {
        Assert.assertEquals(cells.get(cell), got);
    }

    private void whenTryToIGet(String cell) {
        try {
            whenIGet(cell);
        } catch (Exception e) {
            caught = e;
        }
    }

    private void whenIGet(String cell) {
        got = resolve(cell);
    }

    private void whenIRemove_From(String child, String parent) {
        cells.get(parent).remove(child);
    }

    private void given_HasAChild(String parent, String child) {
        Cell cell = resolve(parent);
        cells.put(child, new Cell(child, null, cell));
        cell.add(cells.get(child));
    }

    private void givenACell(String name) {
        cells.put(name, new Cell(name, null, null));
    }

    private void givenACell_Extending(String name, String stem) {
        cells.put(name, new Cell(name, cells.get(stem), null));
    }

    private void thenItShouldThrowTheException(String message) {
        Assert.assertNotNull(caught);
        Assert.assertEquals(message, caught.getMessage());
    }

    private void then_ShouldHave_Children(String cell, int i) {
        int count = 0;
        for (Cell ignored : resolve(cell).children()) {
            count++;
        }
        Assert.assertEquals(i, count);
    }

    private void thenItsFullNameShouldBe(String fullName) {
        Assert.assertEquals(fullName, got.fullName());
    }

}

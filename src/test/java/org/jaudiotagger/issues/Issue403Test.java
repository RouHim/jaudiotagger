package org.jaudiotagger.issues;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.junit.jupiter.api.Test;

public class Issue403Test extends AbstractTestCase {

  @Test
  public void testSetInvalidField() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v1Tag();
      v1Tag.setField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testAddInvalidField() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v1Tag();
      v1Tag.addField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testCreateInvalidField() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v1Tag();
      v1Tag.createField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testDeleteInvalidField() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v1Tag();
      v1Tag.deleteField(FieldKey.DISC_NO);
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    //No need to throw exception because no error ocurred
    assertNull(caught);
  }

  @Test
  public void testSetInvalidFieldV11() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v11Tag();
      v1Tag.setField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testAddInvalidFieldV11() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v11Tag();
      v1Tag.addField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testCreateInvalidFieldV11() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v11Tag();
      v1Tag.createField(FieldKey.DISC_NO, "1");
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    assertTrue(caught instanceof KeyNotFoundException);
  }

  @Test
  public void testDeleteInvalidFieldV11() {
    Exception caught = null;
    try {
      Tag v1Tag = new ID3v11Tag();
      v1Tag.deleteField(FieldKey.DISC_NO);
    } catch (Exception e) {
      caught = e;
      e.printStackTrace();
    }
    //No need to throw exception because no error ocurred
    assertNull(caught);
  }
}

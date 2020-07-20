package com.killrvideo.dse.test;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UidGen {
  
  @Test
  @DisplayName("Generation identifiant")
  public void testIWantAUniqueId() {
    System.out.println(UUID.randomUUID());
  }

}

package com.datastax.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home page
 *
 * <p>List all ticker meta data from table 'ticker_info'. This tabke has been loaded by producer at
 * startup with lines of the CSV.
 */
@Controller
public class MultiChartController {

  @GetMapping("/multichart")
  public String get(Model model) throws Exception {
    return "multichart";
  }
}

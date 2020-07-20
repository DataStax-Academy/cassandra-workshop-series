package com.datastax.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Home page
 *
 * <p>List all ticker meta data from table 'ticker_info'. This tabke has been loaded by producer at
 * startup with lines of the CSV.
 */
@Controller
public class ChartController {

  @GetMapping("/chart/{symbol}")
  public String get(Model model, @PathVariable("symbol") String symbol) throws Exception {
    model.addAttribute("symbol", symbol);
    return "chart";
  }
}

package test;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yufei
 */
public class WindowDestroyer extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}
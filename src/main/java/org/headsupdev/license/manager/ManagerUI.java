/*
 * Copyright 2010-2011 Heads Up Development Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.headsupdev.license.manager;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Document me
 *
 * Created: 15/03/2012
 *
 * @author Andrew Williams
 * @since 1.1
 */
public class ManagerUI extends JFrame
{
    private ManagerConfiguration config;
    private LicenseManager manager;

    public ManagerUI()
        throws HeadlessException
    {
        super( "License Manager loading..." );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // setup OSX support
        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
        System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "License Manager" );
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch ( Exception e )
        {
            // ignore
        }

        setTitle( "License Manager (" + getConfiguration().getLicenseDirectory() + ")" );
        manager = new LicenseManager( getConfiguration() );

        setupKeys();
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( new LicensePanel( manager ), BorderLayout.CENTER );

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu menu = new JMenu( "File" );
        menuBar.add( menu );

        // Create a menu item
        JMenuItem item = new JMenuItem( "Change Directory" );
//        item.addActionListener(actionListener);
        menu.add( item );

        // Install the menu bar in the frame
        setJMenuBar(menuBar);

        setMinimumSize( new Dimension( 200, 350 ) );
        pack();
    }

    public ManagerConfiguration getConfiguration()
    {
        if ( config == null )
        {
            config = new ManagerConfiguration();
        }

        return config;
    }

    private void setupKeys() {
        boolean valid = false;
        try
        {
            valid = manager.verifyKeys();
        }
        catch ( Exception e )
        {
            // ignoring this one
        }
        if ( !valid )
        {
            int result = JOptionPane.showConfirmDialog( this, "You do not have valid keys, should I create them?",
                    "Missing License Keys", JOptionPane.YES_NO_OPTION );
            if ( result == JOptionPane.NO_OPTION )
            {
                return;
            }

            try
            {
                manager.generateKeys();
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( this, "Failed to generate license keys: " + e.getMessage() );
            }
        }
    }
}

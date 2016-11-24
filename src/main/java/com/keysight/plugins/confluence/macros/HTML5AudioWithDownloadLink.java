package com.keysight.plugins.confluence.macros;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.confluence.plugin.services.VelocityHelperService;

public class HTML5AudioWithDownloadLink extends HTML5Audio
{
    @Override
    public boolean getAlwaysIncludeDownloadLink(){ return true; }
    public boolean getNeverIncludeDownloadLink(){ return false; }

    public HTML5AudioWithDownloadLink(AttachmentManager attachmentManager,
	              SettingsManager settingsManager,
		      VelocityHelperService velocityHelperService)
    {
        super( attachmentManager,
               settingsManager,
               velocityHelperService );
    }
}

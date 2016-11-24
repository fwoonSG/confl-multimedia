package com.keysight.plugins.confluence.macros;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.confluence.plugin.services.VelocityHelperService;

public class HTML5AudioWithoutDownloadLink extends HTML5Audio
{
    @Override
    public boolean getAlwaysIncludeDownloadLink(){ return false; }
    public boolean getNeverIncludeDownloadLink(){ return true; }

    public HTML5AudioWithoutDownloadLink(AttachmentManager attachmentManager,
	              SettingsManager settingsManager,
		      VelocityHelperService velocityHelperService)
    {
        super( attachmentManager,
               settingsManager,
               velocityHelperService );
    }
}

package com.keysight.plugins.confluence.macros;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.confluence.plugin.services.VelocityHelperService;

public class HTML5MultimediaWithDownloadLink extends HTML5Audio
{
    @Override
    public boolean getAlwaysIncludeDownloadLink(){ return true; }
    public boolean getNeverIncludeDownloadLink(){ return false; }

    public HTML5MultimediaWithDownloadLink(AttachmentManager attachmentManager,
	              SettingsManager settingsManager,
		      VelocityHelperService velocityHelperService)
    {
        super( attachmentManager,
               settingsManager,
               velocityHelperService );
    }
}

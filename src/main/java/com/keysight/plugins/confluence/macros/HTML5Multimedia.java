package com.keysight.plugins.confluence.macros;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.plugin.services.VelocityHelperService;
import com.atlassian.confluence.velocity.htmlsafe.HtmlFragment;
import com.atlassian.renderer.RenderContext;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.util.AttachmentMimeTypeTranslator;
import com.atlassian.confluence.setup.settings.SettingsManager;

public class HTML5Multimedia implements Macro
{
    private final AttachmentManager attachmentManager;
    private final SettingsManager settingsManager;
    private final VelocityHelperService velocityHelperService;
    
    private boolean isAudio = true;  
    private boolean alwaysIncludeDownloadLink = false;  
    private boolean neverIncludeDownloadLink  = false;  
    
    //Macro Browser Keys
    public static final String MACRO_EDITOR_PAGE_KEY              = "page";
    public static final String MACRO_EDITOR_ATTACHMENT_KEY        = "name";
    public static final String MACRO_EDITOR_POSTER_KEY            = "poster";
    public static final String MACRO_EDITOR_PRESET_KEY            = "preset";
    public static final String MACRO_EDITOR_HEIGHT_KEY            = "height";
    public static final String MACRO_EDITOR_WIDTH_KEY             = "width";
    public static final String MACRO_EDITOR_LOOP_KEY              = "loop";
    public static final String MACRO_EDITOR_AUTOSTART_KEY         = "autostart";
    public static final String MACRO_EDITOR_AUTOPLAY_KEY          = "autoplay";
    public static final String MACRO_EDITOR_ADD_DOWNLOAD_LINK_KEY = "add-download-link";
    public static final String MACRO_EDITOR_IS_AUDIO_KEY          = "is-audio";
    public static final String MACRO_EDITOR_ADD_CONTROLS_KEY      = "add-contols";
    
    //Velocity Template Keys
    public static final String ATTACHMENT_KEY         = "attachment";
    public static final String POSTER_KEY             = MACRO_EDITOR_POSTER_KEY;
    public static final String HEIGHT_KEY             = MACRO_EDITOR_HEIGHT_KEY;
    public static final String WIDTH_KEY              = MACRO_EDITOR_WIDTH_KEY;
    public static final String LOOP_KEY               = MACRO_EDITOR_LOOP_KEY;
    public static final String AUTOPLAY_KEY           = MACRO_EDITOR_AUTOPLAY_KEY;
    public static final String ADD_DOWNLOAD_LINK_KEY  = "addDownloadLink";
    public static final String ADD_CONTROLS_KEY       = "addControls";
    public static final String URL_KEY                = "url";
    public static final String FILE_NAME_KEY          = "filename";
    public static final String FILE_EXTENSION_KEY     = "fileextension";

    public void setIsAudio( Boolean flag ){ this.isAudio = flag; }
    public boolean getIsAudio(){ return this.isAudio; }
    
    public void setAlwaysIncludeDownloadLink( Boolean flag ){
       this.alwaysIncludeDownloadLink = flag; 
       if( flag ){ 
          this.neverIncludeDownloadLink = !flag; 
       }
    }
    public boolean getAlwaysIncludeDownloadLink(){ return this.alwaysIncludeDownloadLink;  }
    
    public void setNeverIncludeDownloadLink( Boolean flag ){
       this.neverIncludeDownloadLink = flag; 
       if( flag ){ 
          this.alwaysIncludeDownloadLink = !flag; 
       }
    }
    public boolean getNeverIncludeDownloadLink(){ return this.neverIncludeDownloadLink;  }
    
    public HTML5Multimedia(AttachmentManager attachmentManager,
		      SettingsManager settingsManager,
		      VelocityHelperService velocityHelperService)
    {
        this.attachmentManager = attachmentManager;
	this.settingsManager = settingsManager;
        this.velocityHelperService = velocityHelperService;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext context)
            throws MacroExecutionException
    {
        Map<String, Object> velocityContext = velocityHelperService.createDefaultVelocityContext();
        
        String videoTemplate = "/templates/html5-video.vm";
        String audioTemplate = "/templates/html5-audio.vm";
        String template = "";

        String fileExtension = "";
        String url           = "";
	
        String page     = (String) parameters.get( MACRO_EDITOR_PAGE_KEY );
	String fileName = (String) parameters.get( MACRO_EDITOR_ATTACHMENT_KEY );
	String baseUrl  = settingsManager.getGlobalSettings().getBaseUrl();

	if( fileName != null ){
	   Attachment attachment  = attachmentManager.getAttachment( context.getEntity(), fileName );
	
           fileExtension = (String) attachment.getFileExtension();   
           url           = baseUrl + (String) attachment.getDownloadPath();

           // force the controls on.
	   velocityContext.put(ADD_CONTROLS_KEY, "controls" );
	   
           velocityContext.put(FILE_NAME_KEY, fileName );
           velocityContext.put(FILE_EXTENSION_KEY, fileExtension ); 	   
	   velocityContext.put(URL_KEY, url );
	   velocityContext.put(URL_KEY, url );
        
	   if( parameters.containsKey(MACRO_EDITOR_POSTER_KEY) ){
              velocityContext.put(POSTER_KEY, (String) parameters.get(MACRO_EDITOR_POSTER_KEY));
           }
	   
           if( parameters.containsKey(MACRO_EDITOR_LOOP_KEY) ){
              velocityContext.put(LOOP_KEY, "true");
           }
           
           if( parameters.containsKey(MACRO_EDITOR_AUTOPLAY_KEY) ){
              velocityContext.put(AUTOPLAY_KEY, "true");
           } else if( parameters.containsKey(MACRO_EDITOR_AUTOSTART_KEY) ){
              velocityContext.put(AUTOPLAY_KEY, "true");
           }
           
           if( getAlwaysIncludeDownloadLink() ){
                 velocityContext.put(ADD_DOWNLOAD_LINK_KEY, "true" );
           } else if( getNeverIncludeDownloadLink() ){
           } else if( parameters.containsKey(MACRO_EDITOR_ADD_DOWNLOAD_LINK_KEY) ){
                 velocityContext.put(ADD_DOWNLOAD_LINK_KEY, "true");
           }

           if( parameters.containsKey(MACRO_EDITOR_PRESET_KEY) ){
              String preset = (String) parameters.get(MACRO_EDITOR_PRESET_KEY);
              if( preset.equals( "640x480 (4:3 VGA)" ) ){
                 velocityContext.put(WIDTH_KEY, "640" );
                 velocityContext.put(HEIGHT_KEY, "480" );
	      } else if( preset.equals( "800x600 (4:3 SVGA)" ) ){
                 velocityContext.put(WIDTH_KEY, "800" );
                 velocityContext.put(HEIGHT_KEY, "600" );
	      } else if( preset.equals( "1024x768 (4:3 XGA)" ) ){
                 velocityContext.put(WIDTH_KEY, "1024" );
                 velocityContext.put(HEIGHT_KEY, "768" );
	      } else if( preset.equals( "1600x1200 (4:3 SXGA+)" ) ){
                 velocityContext.put(WIDTH_KEY, "1600" );
                 velocityContext.put(HEIGHT_KEY, "1200" );
	      } else if( preset.equals( "1280x1024 (5:4 SXGA)" ) ){
                 velocityContext.put(WIDTH_KEY, "1280" );
                 velocityContext.put(HEIGHT_KEY, "1024" );
	      } else if( preset.equals( "640x360 (16:9)" ) ){
                 velocityContext.put(WIDTH_KEY, "640" );
                 velocityContext.put(HEIGHT_KEY, "360" );
	      } else if( preset.equals( "960x540 (16:9 qHD)" ) ){
                 velocityContext.put(WIDTH_KEY, "960" );
                 velocityContext.put(HEIGHT_KEY, "540" );
	      } else if( preset.equals( "1280x720 (16:9 HD)" ) ){
                 velocityContext.put(WIDTH_KEY, "1280" );
                 velocityContext.put(HEIGHT_KEY, "720" );
	      } else if( preset.equals( "1920x1080 (16:9 Full HD)" ) ){
                 velocityContext.put(WIDTH_KEY, "1920" );
                 velocityContext.put(HEIGHT_KEY, "1080" );
              }
           } else {
              if( parameters.containsKey(MACRO_EDITOR_WIDTH_KEY) ){
                 velocityContext.put(WIDTH_KEY, (String) parameters.get(MACRO_EDITOR_WIDTH_KEY));
              }

              if( parameters.containsKey(MACRO_EDITOR_HEIGHT_KEY) ){
                 velocityContext.put(HEIGHT_KEY, (String) parameters.get(MACRO_EDITOR_HEIGHT_KEY));
              }
           }
	}

        if( parameters.containsKey(MACRO_EDITOR_IS_AUDIO_KEY) ){
           setIsAudio( true );
        } else if( fileExtension.equals( "mp3" ) || fileExtension.equals( "wav" ) ) {
           setIsAudio( true );
        } else {
           setIsAudio( false );
        }

        if(getIsAudio() ){
           template = audioTemplate;
        } else {
           template = videoTemplate;
        }

        return velocityHelperService.getRenderedTemplate(template, velocityContext);
    }
    
    @Override
    public BodyType getBodyType()
    {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }
}

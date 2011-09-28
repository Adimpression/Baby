package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.logic.Listeners.widgets.DisplayName;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.Listeners.widgets.UserPropertySidebar;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.Loggers.EXCEPTION;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 12, 2010
 * Time: 9:54:25 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class AbstractSkeletonListener extends AbstractListener {
    private static final String BN = "bn";
    protected static final ResourceBundle GUI = RBGet.gui();
    private static final String CODENAME_KEY = "codename";
    boolean initStatus = false;
    private static final String TALK = "Get in touch";

    /**
     * @param request_
     */
    public AbstractSkeletonListener(final ItsNatServletRequest request_) {
        super(request_, request_);
    }

    /**
     * Initializes your document here by appending fragments.
     * <p/>
     * If/when you are overriding this method, remember to call super.init(with required params)  so that,
     * the template is initialized. You have the flexibility as to when to call this method when doing so. i.e. Either
     * after you're done with your own init work, or before doing it, or in the middle!
     *
     * @param itsNatHTMLDocument__
     * @param hTMLDocument__
     * @param itsNatDocument__
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
        if (initStatus) {
            throw ExceptionCache.MULTIPLE_INITS;
        }

        initStatus = true;

        final ItsNatServletRequest request__ = (ItsNatServletRequest) initArgs[0];

        itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

        layoutNeededForAllPages:
        {
            setLoginWidget:
            {
                try {
                    new SignInOn(request__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                    };
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
            setTitle:
            {
                try {
                    setMainTitle:
                    {
                        $(skeletonTitle).setTextContent(
                                RBGet.globalConfig.getString(CODENAME_KEY));
                    }
                    setMetaDescription:
                    {
                        $(skeletonTitle).setAttribute(MarkupTag.META.namee(),
                                RBGet.globalConfig.getString(CODENAME_KEY));
                    }
                } catch (final Throwable t) {
                    Loggers.DEBUG.debug(t.getMessage());
                }
            }

            setProfileLink:
            {
                try {
                    if (getUsername() != null) {
                        $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.Profile.getURL());
                    } else {
                        $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
            setProfileDataLink:
            {
                try {
                    if (getUsername() != null) {
                        final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(new HumanId(getUsername()).getSelfAsValid());
                        if (r.returnStatus() == 0) {
                            final HumansIdentity hi = r.returnValue();
                            String profilePhotoURL = hi.getHumansIdentityProfilePhoto();
                            $(Skeleton_profile_photo).setAttribute(
                                    MarkupTag.IMG.src(),
                                    ai.ilikeplaces.logic.Listeners.widgets.UserProperty.formatProfilePhotoUrlStatic(profilePhotoURL)
                            );
                            $(Skeleton_othersidebar_wall_link).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
                        }
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
            sideBarFriends:
            {
                try {
                    if (getUsername() != null) {
                        final HumansNetPeople humansNetPeople = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(new HumanId(getUsernameAsValid()).getSelfAsValid());

                        for (final HumansNetPeople friend : humansNetPeople.getHumansNetPeoples()) {
                            new UserPropertySidebar(request__, $(Controller.Page.Skeleton_sidebar), new HumanId(friend.getHumanId())) {
                                protected void init(final Object... initArgs) {
                                    $$(Controller.Page.user_property_sidebar_content).appendChild(
                                            ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(TALK).$ElementSetHref(ProfileRedirect.PROFILE_URL + friend.getHumanId()).get()
                                    );
                                }
                            };
                        }
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
            signinupActionNotice:
            {
                try {
                    if (getUsername() == null) {
                        //We do nothing now
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
        }
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     * <p/>
     * If/when you are overriding this method, remember to call super.registerEventListeners(with required params)  so that,
     *
     * @param itsNatHTMLDocument_
     * @param itsNatDocument__
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_, final ItsNatDocument itsNatDocument__) {
    }

    protected void setLoginWidget(final ItsNatServletRequest request__) {
        initStatus = true;

        try {
            new SignInOn(request__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
            };
        } catch (final Throwable t) {
            EXCEPTION.error("{}", t);
        }
    }

    protected void setTitle(final String customTitle) {
        initStatus = true;

        try {
            setMainTitle:
            {
                if (customTitle != null) {
                    $(skeletonTitle).setTextContent(customTitle);
                } else {
                    $(skeletonTitle).setTextContent(RBGet.globalConfig.getString(BN));
                }
            }
            setMetaDescription:
            {
                if (customTitle != null) {
                    $(skeletonTitle).setAttribute(MarkupTag.META.namee(),customTitle);
                } else {
                    $(skeletonTitle).setAttribute(MarkupTag.META.namee(), RBGet.globalConfig.getString(BN));
                }
            }
        } catch (final Throwable t) {
            Loggers.DEBUG.debug(t.getMessage());
        }
    }

    protected void setProfileLink() {
        initStatus = true;

        try {
            if (getUsername() != null) {
                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.Profile.getURL());
            } else {
                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
            }
        } catch (final Throwable t) {
            EXCEPTION.error("{}", t);
        }
    }

    protected void setProfileDataLink() {
        initStatus = true;


        try {
            if (getUsername() != null) {
                final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(new HumanId(getUsername()).getSelfAsValid());
                if (r.returnStatus() == 0) {
                    final HumansIdentity hi = r.returnValue();
                    $(Skeleton_profile_photo).setAttribute(MarkupTag.IMG.src(), ai.ilikeplaces.logic.Listeners.widgets.UserProperty.formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));
                    $(Skeleton_othersidebar_wall_link).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
                }
            }
        } catch (final Throwable t) {
            EXCEPTION.error("{}", t);
        }
    }

    protected void setSideBarFriends(final ItsNatServletRequest request__) {
        initStatus = true;

        try {
            if (getUsername() != null) {
                final HumansNetPeople humansNetPeople = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(new HumanId(getUsernameAsValid()).getSelfAsValid());

                for (final HumansNetPeople friend : humansNetPeople.getHumansNetPeoples()) {
                    new UserPropertySidebar(request__, $(Controller.Page.Skeleton_sidebar), new HumanId(friend.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_sidebar_content).setTextContent(TALK);
                        }
                    };
                }
            }
        } catch (final Throwable t) {
            EXCEPTION.error("{}", t);
        }
    }
}

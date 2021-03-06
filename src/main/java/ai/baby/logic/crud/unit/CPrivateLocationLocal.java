package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBFetchDataException;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CPrivateLocationLocal {

    @Deprecated
    public PrivateLocation doNTxCPrivateLocation(final String humanId, final String locationName, final String locationInfo) throws DBFetchDataException, DBRefreshDataException;

    public PrivateLocation doNTxCPrivateLocation(final String humanId, final String locationName, final String locationInfo, final Double latitude, final Double longitude) throws DBFetchDataException, DBRefreshDataException;

}

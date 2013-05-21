package org.ei.drishti.service;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.ei.drishti.domain.Alert;
import org.ei.drishti.dto.Action;
import org.ei.drishti.dto.BeneficiaryType;
import org.ei.drishti.repository.AlertRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.HashMap;

import static org.ei.drishti.dto.AlertStatus.normal;
import static org.ei.drishti.dto.AlertStatus.urgent;
import static org.ei.drishti.util.ActionBuilder.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AlertServiceTest {
    @Mock
    private AlertRepository alertRepository;

    private AlertService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new AlertService(alertRepository);
    }

    @Test
    public void shouldAddAnAlertIntoAlertRepositoryForMotherCreateAlertAction() throws Exception {
        Action actionForMother = actionForCreateAlert("Case X", normal.value(), BeneficiaryType.mother.value(), "Schedule 1", "ANC 1", "2012-01-01", "2012-01-22", "0");

        service.create(actionForMother);

        verify(alertRepository).createAlert(new Alert("Case X", "ANC 1", normal, "2012-01-01", "2012-01-22"));
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    public void shouldAddAnAlertIntoAlertRepositoryForECCreateAlertAction() throws Exception {
        Action actionForEC = actionForCreateAlert("Case X", normal.value(), BeneficiaryType.ec.value(), "Schedule 1", "Milestone 1", "2012-01-01", "2012-01-22", "0");

        service.create(actionForEC);

        verify(alertRepository).createAlert(new Alert("Case X", "Milestone 1", normal, "2012-01-01", "2012-01-22"));
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    public void shouldAddAnAlertIntoAlertRepositoryForECCreateAlertActionWhenThereIsNoMother() throws Exception {
        Action actionForEC = actionForCreateAlert("Case X", normal.value(), BeneficiaryType.ec.value(), "Schedule 1", "Milestone 1", "2012-01-01", "2012-01-22", "0");

        service.create(actionForEC);

        verify(alertRepository).createAlert(new Alert("Case X", "Milestone 1", normal, "2012-01-01", "2012-01-22"));
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    public void shouldNotCreateIfActionIsInactive() throws Exception {
        Action actionForMother = new Action("Case X", "alert", "createAlert", new HashMap<String, String>(), "0", false, new HashMap<String, String>());

        service.create(actionForMother);

        verifyZeroInteractions(alertRepository);
    }

    @Test
    public void shouldAddAnAlertIntoAlertRepositoryForChildCreateAlertAction() throws Exception {
        Action actionForMother = actionForCreateAlert("Case X", urgent.value(), BeneficiaryType.child.value(), "Schedule 1", "Milestone 1", "2012-01-01", "2012-01-22", "0");

        service.create(actionForMother);

        verify(alertRepository).createAlert(new Alert("Case X", "Milestone 1", urgent, "2012-01-01", "2012-01-22"));
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    public void shouldMarkAlertAsClosedInRepositoryForCloseActions() throws Exception {
        Action firstAction = actionForCloseAlert("Case X", "ANC 1", "2012-01-01", "0");
        Action secondAction = actionForCloseAlert("Case Y", "ANC 2", "2012-01-01", "0");

        service.close(firstAction);
        service.close(secondAction);

        verify(alertRepository).markAlertAsClosed("Case X", "ANC 1", "2012-01-01");
        verify(alertRepository).markAlertAsClosed("Case Y", "ANC 2", "2012-01-01");
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    public void shouldDeleteAllFromRepositoryForDeleteAllActions() throws Exception {
        Action firstAction = actionForDeleteAllAlert("Case X");
        Action secondAction = actionForDeleteAllAlert("Case Y");

        service.deleteAll(firstAction);
        service.deleteAll(secondAction);

        verify(alertRepository).deleteAllAlertsForCase("Case X");
        verify(alertRepository).deleteAllAlertsForCase("Case Y");
        verifyNoMoreInteractions(alertRepository);
    }
}

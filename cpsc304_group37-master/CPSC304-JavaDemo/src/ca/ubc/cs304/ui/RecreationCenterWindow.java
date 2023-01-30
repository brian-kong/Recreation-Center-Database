package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RecreationCenterDelegate;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class RecreationCenterWindow extends JFrame{
	public RecreationCenterWindow() {
		super("Recreation Center Window");
	}

	public void showFrame(RecreationCenterDelegate delegate) {
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textArea.setEditable(false);

		JButton viewAllCustomersBtn = new JButton("View All Customers");
		viewAllCustomersBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewAllCustomers();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteCustomerBtn = new JButton("Delete Customer");
		deleteCustomerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> customerIDs = delegate.findAllCustomerIDs();
					String customerID = promptInputCustomerID(customerIDs);
					if (customerID != null) {
						delegate.deleteCustomer(customerID);
					}
					List<String[]> res = delegate.viewAllCustomers();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addCustomerBtn = new JButton("Add Customer");
		addCustomerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> membershipTypes = delegate.findAllMembershipTypes();
					String[] customerInputs = promptInputCustomerInfo(membershipTypes);
					if (customerInputs != null) {
						delegate.addNewCustomer(customerInputs[0], customerInputs[1], customerInputs[2], customerInputs[3], customerInputs[4], customerInputs[5]);
					}
					List<String[]> res = delegate.viewAllCustomers();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton updateCustomerBtn = new JButton("Update Customer");
		updateCustomerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> customerIDs = delegate.findAllCustomerIDs();
					List<String> membershipTypes = delegate.findAllMembershipTypes();
					String[] customerInputs = promptInputUpdateCustomer(customerIDs, membershipTypes);
					if (customerInputs != null) {
						delegate.updateCustomer(customerInputs[0], customerInputs[1], customerInputs[2], customerInputs[3], customerInputs[4], customerInputs[5]);
					}
					List<String[]> res = delegate.viewAllCustomers();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewAllMembershipsBtn = new JButton("View Memberships");
		viewAllMembershipsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewAllMemberships();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteMembershipBtn = new JButton("Delete Membership");
		deleteMembershipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> membershipTypes = delegate.findAllMembershipTypes();
					String membershipType = promptInputMembershipType(membershipTypes);
					if (membershipType != null) {
						delegate.deleteMembership(membershipType);
					}
					List<String[]> res = delegate.viewAllMemberships();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addMembershipBtn = new JButton("Add Membership");
		addMembershipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String[] membershipInputs = promptInputMembershipInfo();
					if (membershipInputs != null) {
						delegate.addNewMembership(membershipInputs[0], membershipInputs[1]);
					}
					List<String[]> res = delegate.viewAllMemberships();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton updateMembershipBtn = new JButton("Update Membership");
		updateMembershipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> membershipTypes = delegate.findAllMembershipTypes();
					String[] membershipInputs = promptInputUpdateMembership(membershipTypes);
					if (membershipInputs != null) {
						delegate.updateMembership(membershipInputs[0], membershipInputs[1]);
					}
					List<String[]> res = delegate.viewAllMemberships();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton findMembershipBtn = new JButton("Find Membership");
		findMembershipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String membershipAggregation = promptFindMembership();
					if (membershipAggregation != null) {
						List<String[]> res = delegate.findMembership(membershipAggregation);
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewAllEventsBtn = new JButton("View All Events");
		viewAllEventsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewAllEvents();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteEventBtn = new JButton("Delete Event");
		deleteEventBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventNames = delegate.findAllEventNames();
					List<String> eventDates = delegate.findAllEventDates();
					String[] eventKeys = promptEventNameDate(eventNames, eventDates);
					if (eventKeys != null) {
						delegate.deleteEvent(eventKeys[0], eventKeys[1]);
					}
					List<String[]> res = delegate.viewAllEvents();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addEventBtn = new JButton("Add Event");
		addEventBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventLocations = delegate.findAllLocations();
					String[] eventInputs = promptInputEventInfo(eventLocations);
					if (eventInputs != null) {
						delegate.addNewEvent(eventInputs[0], eventInputs[1], eventInputs[2], eventInputs[3]);
					}
					List<String[]> res = delegate.viewAllEvents();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton updateEventBtn = new JButton("Update Event");
		updateEventBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventNames = delegate.findAllEventNames();
					List<String> eventDates = delegate.findAllEventDates();
					List<String> eventLocations = delegate.findAllLocations();
					String[] eventInputs = promptEventUpdateInfo(eventNames, eventDates, eventLocations);
					if (eventInputs != null) {
						delegate.updateEvent(eventInputs[0], eventInputs[1], eventInputs[2], eventInputs[3]);
					}
					List<String[]> res = delegate.viewAllEvents();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewParticipations = new JButton("View Participations");
		viewParticipations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewParticipations();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewAllEventsParticipationBtn = new JButton("View Event Participants");
		viewAllEventsParticipationBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventNames = delegate.findAllEventNames();
					List<String> eventDates = delegate.findAllEventDates();
					String[] eventKeys = promptEventNameDate(eventNames, eventDates);
					if (eventKeys != null) {
						List<String[]> res = delegate.viewAllParticipantsForTheEvent(eventKeys[0], eventKeys[1]);
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteParticipantFromEventBtn = new JButton("Delete Participant");
		deleteParticipantFromEventBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventNames = delegate.findAllEventNames();
					List<String> eventDates = delegate.findAllEventDates();
					List<String> customerIDs = delegate.findAllCustomerIDs();
					String[] participationInputs = promptInputEventParticipationInfo(eventNames, eventDates, customerIDs);
					if (participationInputs != null) {
						delegate.deleteEventParticipant(participationInputs[0], participationInputs[1], participationInputs[2]);
						List<String[]> res = delegate.viewAllParticipantsForTheEvent(participationInputs[0], participationInputs[1]);
						displayResult(res, scrollPane);
					} else {
						List<String[]> res = delegate.viewParticipations();
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addParticipantsToEventBtn = new JButton("Add Participant");
		addParticipantsToEventBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> eventNames = delegate.findAllEventNames();
					List<String> eventDates = delegate.findAllEventDates();
					List<String> customerIDs = delegate.findAllCustomerIDs();
					String[] participationInputs = promptInputEventParticipationInfo(eventNames, eventDates, customerIDs);
					if (participationInputs != null) {
						delegate.addNewEventParticipant(participationInputs[0], participationInputs[1], participationInputs[2]);
						List<String[]> res = delegate.viewAllParticipantsForTheEvent(participationInputs[0], participationInputs[1]);
						displayResult(res, scrollPane);
					} else {
						List<String[]> res = delegate.viewParticipations();
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton findCustomersBtn = new JButton("Find Customers Participation");
		findCustomersBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String participation = promptCustomerParticipation();
					if (participation != null) {
						List<String[]> res = delegate.findCustomers(participation);;
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewAllFitnessClassesBtn = new JButton("View Fitness Classes");
		viewAllFitnessClassesBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewFitnessClasses();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});
		JButton viewRegistrations = new JButton("View Registrations");
		viewRegistrations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewRegistrations();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});
		JButton viewDetailedScheduleBtn = new JButton("View Schedule with More Details");
		viewDetailedScheduleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewDetailedClassSchedule();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});
		JButton viewPopularClassesBtn = new JButton("View classes by popularity");
		viewPopularClassesBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewMostPopularClasses();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});
		JButton viewAvgClassesBtn = new JButton("View avg num customers in a class");
		viewAvgClassesBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewAvgNumCustomersInClasses();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteFitnessClassBtn = new JButton("Delete Fitness Class");
		deleteFitnessClassBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> fitnessClassNames = delegate.findAllFitnessClassNames();
					String fcName = promptInputFitnessClassName(fitnessClassNames);
					if (fcName != null) {
						delegate.deleteFitnessClass(fcName);
					}
					List<String[]> res = delegate.viewFitnessClasses();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addFitnessClassBtn = new JButton("Add Fitness Class");
		addFitnessClassBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String[] fitnessClassInputs = promptInputFitnessClassInfo();
				try {
					delegate.insertFitnessClass(fitnessClassInputs[0], fitnessClassInputs[1]);
					List<String[]> res = delegate.viewFitnessClasses();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteRegistrationBtn = new JButton("Unregister from a class");
		deleteRegistrationBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> fitnessClassNamesWithDays = delegate.findAllFitnessClassesWithDays();
					String[] registrationInputs = promptInputRegistration(fitnessClassNamesWithDays);
					if (registrationInputs != null) {
						delegate.deleteRegistersIn(registrationInputs[0], registrationInputs[1], registrationInputs[2]);
					}
					List<String[]> res = delegate.viewRegistrations();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});
		JButton addRegistrationBtn = new JButton("Register for a class");
		addRegistrationBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> fitnessClassNamesWithDays = delegate.findAllFitnessClassesWithDays();
					String[] registrationInputs = promptInputRegistration(fitnessClassNamesWithDays);
					if (registrationInputs != null) {
						delegate.insertRegistersIn(registrationInputs[0], registrationInputs[1], registrationInputs[2]);
					}
					List<String[]> res = delegate.viewRegistrations();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton viewAllFacilityBtn = new JButton("View All Facility");
		viewAllFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String[]> res = delegate.viewAllFacility();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton deleteFacilityBtn = new JButton("Delete Facility");
		deleteFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> facilityNames = delegate.findAllLocations();
					String facilityName = promptInputFacilityLocation(facilityNames);
					if (facilityName != null) {
						delegate.deleteFacility(facilityName);
					}
					List<String[]> res = delegate.viewAllFacility();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton addFacilityBtn = new JButton("Add Facility");
		addFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String[] facilityInputs = promptInputFacilityInfo();
					if (facilityInputs != null) {
						delegate.addNewFacility(facilityInputs[0], facilityInputs[1], facilityInputs[2], facilityInputs[3]);
					}
					List<String[]> res = delegate.viewAllFacility();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton updateFacilityBtn = new JButton("Update Facility");
		updateFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> facilityNames = delegate.findAllLocations();
					String[] facilityInputs = promptUpdateFacilityInfo(facilityNames);
					if (facilityInputs != null) {
						delegate.updateFacility(facilityInputs[0], facilityInputs[1], facilityInputs[2], facilityInputs[3]);
					}
					List<String[]> res = delegate.viewAllFacility();
					displayResult(res, scrollPane);
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton selectFacilityBtn = new JButton("Select Facility");
		selectFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String[] facilityInputs = promptSelectFacility();
					if (facilityInputs != null) {
						List<String[]> res = delegate.selectFacility(facilityInputs[0], facilityInputs[1], facilityInputs[2]);
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton projectFacilityBtn = new JButton("Project Facility");
		projectFacilityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String facilityProject = promptProjectFacility();
					if (facilityProject != null) {
						List<String[]> res = delegate.projectFacility(facilityProject);
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JButton joinBtn = new JButton("Join Tables");
		joinBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					List<String> tableNames = delegate.findAllTables();
					String[] tablesToJoin = promptTablesJoin(tableNames);
					if (tablesToJoin != null) {
						List<String[]> res = delegate.joinTables(tablesToJoin[0], tablesToJoin[1]);
						displayResult(res, scrollPane);
					}
				} catch (Exception e) {
					displayErrorMsg(e.getMessage());
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		JMenu customerMenu = new JMenu("Customers");
		JMenu membershipMenu = new JMenu("Memberships");
		JMenu eventsMenu = new JMenu("Events");
		JMenu participationMenu = new JMenu("Participants");
		JMenu fitnessClassMenu = new JMenu("Fitness Classes");
		JMenu facilityMenu = new JMenu ("Facility");
		menuBar.add(customerMenu);
		menuBar.add(membershipMenu);
		menuBar.add(eventsMenu);
		menuBar.add(fitnessClassMenu);
		menuBar.add(facilityMenu);
		menuBar.add(joinBtn);

		customerMenu.add(viewAllCustomersBtn);
		customerMenu.add(deleteCustomerBtn);
		customerMenu.add(addCustomerBtn);
		customerMenu.add(updateCustomerBtn);

		membershipMenu.add(viewAllMembershipsBtn);
		membershipMenu.add(deleteMembershipBtn);
		membershipMenu.add(addMembershipBtn);
		membershipMenu.add(updateMembershipBtn);
		membershipMenu.add(findMembershipBtn);

		eventsMenu.add(viewAllEventsBtn);
		eventsMenu.add(deleteEventBtn);
		eventsMenu.add(addEventBtn);
		eventsMenu.add(updateEventBtn);
		eventsMenu.add(participationMenu);

		participationMenu.add(viewParticipations);
		participationMenu.add(viewAllEventsParticipationBtn);
		participationMenu.add(deleteParticipantFromEventBtn);
		participationMenu.add(addParticipantsToEventBtn);
		participationMenu.add(findCustomersBtn);

		fitnessClassMenu.add(addFitnessClassBtn);
		fitnessClassMenu.add(deleteFitnessClassBtn);
		fitnessClassMenu.add(addRegistrationBtn);
		fitnessClassMenu.add(deleteRegistrationBtn);
		fitnessClassMenu.add(viewAllFitnessClassesBtn);
		fitnessClassMenu.add(viewRegistrations);
		fitnessClassMenu.add(viewDetailedScheduleBtn);
		fitnessClassMenu.add(viewPopularClassesBtn);
		fitnessClassMenu.add(viewAvgClassesBtn);

		facilityMenu.add(viewAllFacilityBtn);
		facilityMenu.add(deleteFacilityBtn);
		facilityMenu.add(addFacilityBtn);
		facilityMenu.add(updateFacilityBtn);
		facilityMenu.add(selectFacilityBtn);
		facilityMenu.add(projectFacilityBtn);

		this.getContentPane().add(BorderLayout.NORTH, menuBar);
		this.getContentPane().add(BorderLayout.CENTER, scrollPane);
		this.setVisible(true);
	}

	private String[] promptInputCustomerInfo(List<String> membershipTypes) {
		String[] result = new String[6];
		String[] convertedMembershipTypes = membershipTypes.toArray(new String[0]);
		JComboBox<String> membershipTypesList = new JComboBox<>(convertedMembershipTypes);
		JTextField customerIDText = new JTextField(5);
		JTextField customerNameText = new JTextField(5);
		JTextField customerAddressText = new JTextField(5);
		JTextField customerPostalCodeText = new JTextField(5);
		JTextField customerPhoneNumberText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Customer ID:"));
		myPanel.add(customerIDText);
		myPanel.add(new JLabel("Name:"));
		myPanel.add(customerNameText);
		myPanel.add(new JLabel("Address:"));
		myPanel.add(customerAddressText);
		myPanel.add(new JLabel("Postal Code:"));
		myPanel.add(customerPostalCodeText);
		myPanel.add(new JLabel("Phone Number:"));
		myPanel.add(customerPhoneNumberText);
		myPanel.add(new JLabel("Membership Type:"));
		myPanel.add(membershipTypesList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Add Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result[0] = customerIDText.getText();
			result[1] = customerNameText.getText();
			result[2] = customerAddressText.getText();
			result[3] = customerPostalCodeText.getText();
			result[4] = customerPhoneNumberText.getText();
			result[5] = (String) membershipTypesList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}
		return result;
	}

	private String promptInputCustomerID(List<String> customerIDs) {
		String result = new String();
		String[] convertedIDs = customerIDs.toArray(new String[0]);
		JComboBox<String> customerIDList = new JComboBox<>(convertedIDs);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Customer to Delete:"));
		myPanel.add(customerIDList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Delete Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result =  (String) customerIDList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputUpdateCustomer(List<String> customerIDs, List<String> membershipTypes) {
		String[] result = new String[6];
		String[] convertedIDs = customerIDs.toArray(new String[0]);
		String[] convertedMembershipTypes = membershipTypes.toArray(new String[0]);
		JComboBox<String> customerIDList = new JComboBox<>(convertedIDs);
		JComboBox<String> membershipTypesList = new JComboBox<>(convertedMembershipTypes);
		JTextField customerNameText = new JTextField(5);
		JTextField customerAddressText = new JTextField(5);
		JTextField customerPostalCodeText = new JTextField(5);
		JTextField customerPhoneNumberText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Customer ID:"));
		myPanel.add(customerIDList);
		myPanel.add(new JLabel("Name:"));
		myPanel.add(customerNameText);
		myPanel.add(new JLabel("Address:"));
		myPanel.add(customerAddressText);
		myPanel.add(new JLabel("Postal Code:"));
		myPanel.add(customerPostalCodeText);
		myPanel.add(new JLabel("Phone Number:"));
		myPanel.add(customerPhoneNumberText);
		myPanel.add(new JLabel("Membership Type:"));
		myPanel.add(membershipTypesList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Customer Update", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result[0] =  (String) customerIDList.getSelectedItem();
			result[1] = customerNameText.getText();
			result[2] = customerAddressText.getText();
			result[3] = customerPostalCodeText.getText();
			result[4] = customerPhoneNumberText.getText();
			result[5] = (String) membershipTypesList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputMembershipInfo() {
		String[] result = new String[2];
		JTextField membershipTypeText = new JTextField(5);
		JTextField memebrshipPriceText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Membership Type:"));
		myPanel.add(membershipTypeText);
		myPanel.add(new JLabel("Price per month:"));
		myPanel.add(memebrshipPriceText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Add membership", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = membershipTypeText.getText();
			result[1] = memebrshipPriceText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String promptInputMembershipType(List<String> membershipTypes) {
		String result = new String();
		String[] convertedMTypes = membershipTypes.toArray(new String[0]);
		JComboBox<String> membershipList = new JComboBox<>(convertedMTypes);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Membership Type To Delete:"));
		myPanel.add(membershipList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Delete Membership", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result =  (String) membershipList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputUpdateMembership(List<String> membershipTypes) {
		String[] result = new String[2];
		String[] convertedMTypes = membershipTypes.toArray(new String[0]);
		JComboBox<String> membershipList = new JComboBox<>(convertedMTypes);
		JTextField memebrshipPriceText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Membership Type To Update:"));
		myPanel.add(membershipList);
		myPanel.add(new JLabel("Price per month:"));
		myPanel.add(memebrshipPriceText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Update Membership", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result[0] =  (String) membershipList.getSelectedItem();
			result[1] = memebrshipPriceText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String promptFindMembership() {
		String result = new String();
		String[] maxOrMin = { "max", "min"};
		JComboBox aggregationChoice = new JComboBox(maxOrMin);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Where price is: "));
		myPanel.add(aggregationChoice);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Find Membership", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result =  (String) aggregationChoice.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptEventNameDate(List<String> eventName, List<String> eventDate) {
		String[] result = new String[2];

		String[] convertedEventName = eventName.toArray(new String[0]);
		String[] convertedEventDate = eventDate.toArray(new String[0]);
		JComboBox<String> eventNameList = new JComboBox<>(convertedEventName);
		JComboBox<String> eventDateList = new JComboBox<>(convertedEventDate);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Event Name:"));
		myPanel.add(eventNameList);
		myPanel.add(new JLabel("Event Date:"));
		myPanel.add(eventDateList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Event Identification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = (String) eventNameList.getSelectedItem();
			result[1] = (String) eventDateList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptEventUpdateInfo(List<String> eventName, List<String> eventDate, List<String> locations) {
		String[] result = new String[4];

		String[] convertedEventName = eventName.toArray(new String[0]);
		String[] convertedEventDate = eventDate.toArray(new String[0]);
		String[] convertedLocations = locations.toArray(new String[0]);
		JComboBox<String> eventNameList = new JComboBox<>(convertedEventName);
		JComboBox<String> eventDateList = new JComboBox<>(convertedEventDate);
		JComboBox<String> locationList = new JComboBox<>(convertedLocations);
		JTextField eventThemeText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Event Name:"));
		myPanel.add(eventNameList);
		myPanel.add(new JLabel("Event Date:"));
		myPanel.add(eventDateList);
		myPanel.add(new JLabel("Thematic:"));
		myPanel.add(eventThemeText);
		myPanel.add(new JLabel("Location:"));
		myPanel.add(locationList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Update Event", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = (String) eventNameList.getSelectedItem();
			result[1] = (String) eventDateList.getSelectedItem();
			result[2] = eventThemeText.getText();
			result[3] = (String) locationList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputEventInfo(List<String> locations) {
		String[] result = new String[4];
		JTextField eventNameText = new JTextField(5);
		JTextField eventDateText = new JTextField(5);
		JTextField eventThemeText = new JTextField(5);
		String[] convertedLocations = locations.toArray(new String[0]);
		JComboBox<String> locationList = new JComboBox<>(convertedLocations);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Name:"));
		myPanel.add(eventNameText);
		myPanel.add(new JLabel("Date (YYYY-MM-DD):"));
		myPanel.add(eventDateText);
		myPanel.add(new JLabel("Thematic:"));
		myPanel.add(eventThemeText);
		myPanel.add(new JLabel("Location:"));
		myPanel.add(locationList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Event Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = eventNameText.getText();
			result[1] = eventDateText.getText();
			result[2] = eventThemeText.getText();
			result[3] = (String) locationList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputEventParticipationInfo(List<String> eventName, List<String> eventDate, List<String> customerIDs) {
		String[] result = new String[3];

		String[] convertedEventName = eventName.toArray(new String[0]);
		String[] convertedEventDate = eventDate.toArray(new String[0]);
		String[] convertedCustomerIDs = customerIDs.toArray(new String[0]);
		JComboBox<String> eventNameList = new JComboBox<>(convertedEventName);
		JComboBox<String> eventDateList = new JComboBox<>(convertedEventDate);
		JComboBox<String> customerIDList = new JComboBox<>(convertedCustomerIDs);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Event Name:"));
		myPanel.add(eventNameList);
		myPanel.add(new JLabel("Event Date (YYYY-MM-DD):"));
		myPanel.add(eventDateList);
		myPanel.add(new JLabel("Customer ID:"));
		myPanel.add(customerIDList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Event Participation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = (String) eventNameList.getSelectedItem();
			result[1] = (String) eventDateList.getSelectedItem();
			result[2] = (String) customerIDList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String promptCustomerParticipation() {
		String result = new String();
		String[] maxOrMin = { "All of the Events", "None of the Events"};
		JComboBox aggregationChoice = new JComboBox(maxOrMin);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Customers who participates in: "));
		myPanel.add(aggregationChoice);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Find Customers", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result =  (String) aggregationChoice.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}
//----------------------------------------------------------------------------------------
	private String promptInputFacilityLocation(List<String> facilityNames) {
		String result = new String();
		String[] convertedFacilityNames = facilityNames.toArray(new String[0]);
		JComboBox<String> facilityNamesList = new JComboBox<>(convertedFacilityNames);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Facility Name:"));
		myPanel.add(facilityNamesList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Delete Facility", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result = (String) facilityNamesList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptInputFacilityInfo() {
		String[] result = new String[4];
		JTextField facilityNameText = new JTextField(5);
		JTextField florNumText = new JTextField(5);
		JTextField facilitySizeText = new JTextField(5);
		JTextField facilityHoursText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Name:"));
		myPanel.add(facilityNameText);
		myPanel.add(new JLabel("Floor number:"));
		myPanel.add(florNumText);
		myPanel.add(new JLabel("Facility size:"));
		myPanel.add(facilitySizeText);
		myPanel.add(new JLabel("Facility hours:"));
		myPanel.add(facilityHoursText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Add Facility", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = facilityNameText.getText();
			result[1] = florNumText.getText();
			result[2] = facilitySizeText.getText();
			result[3] = facilityHoursText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptUpdateFacilityInfo(List<String> facilityNames) {
		String[] result = new String[4];
		JTextField florNumText = new JTextField(5);
		JTextField facilitySizeText = new JTextField(5);
		JTextField facilityHoursText = new JTextField(5);
		String[] convertedFacilityNames = facilityNames.toArray(new String[0]);
		JComboBox<String> facilityNamesList = new JComboBox<>(convertedFacilityNames);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Facility Name:"));
		myPanel.add(facilityNamesList);
		myPanel.add(new JLabel("Floor number:"));
		myPanel.add(florNumText);
		myPanel.add(new JLabel("Floor size:"));
		myPanel.add(facilitySizeText);
		myPanel.add(new JLabel("Floor hours:"));
		myPanel.add(facilityHoursText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Update Facility", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = (String) facilityNamesList.getSelectedItem();
			result[1] = florNumText.getText();
			result[2] = facilitySizeText.getText();
			result[3] = facilityHoursText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptSelectFacility() {
		String[] result = new String[3];
		String[] attrs = { "floor", "facility size"};
		JComboBox selectionChoice = new JComboBox(attrs);
		String[] inequalitySigns = { "<", "=", ">"};
		JComboBox signChoice = new JComboBox(inequalitySigns);
		JTextField numberText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Where:"));
		myPanel.add(selectionChoice);
		myPanel.add(new JLabel("Is"));
		myPanel.add(signChoice);
		myPanel.add(new JLabel("Than/To:"));
		myPanel.add(numberText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Find Facility", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result[0] =  (String) selectionChoice.getSelectedItem();
			result[1] =  (String) signChoice.getSelectedItem();
			result[2] = numberText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String promptProjectFacility() {
		String result = new String();
		String valuesPicked = "";
		JCheckBox nameCheckbox = new JCheckBox("Name");
		JCheckBox floorCheckbox = new JCheckBox("Floor");
		JCheckBox sizeCheckbox = new JCheckBox("Size");
		JCheckBox hoursCheckbox = new JCheckBox("Hours");

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Attributes to project:"));
		myPanel.add(nameCheckbox);
		myPanel.add(floorCheckbox);
		myPanel.add(sizeCheckbox);
		myPanel.add(hoursCheckbox);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Project Facility Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			if (nameCheckbox.isSelected()) {
				valuesPicked += " facilityName,";
			}
			if (floorCheckbox.isSelected()) {
				valuesPicked += " floorNum,";
			}
			if (sizeCheckbox.isSelected()) {
				valuesPicked += " fsize,";
			}
			if (hoursCheckbox.isSelected()) {
				valuesPicked += " fhours,";
			}
		}

		if (!valuesPicked.isEmpty()) {
			result = valuesPicked.substring(0, valuesPicked.length() - 1);
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}
	//------------------------------------------------------------------------------------
	private String[] promptInputRegistration(List<String> fitnessClassNamesWithDays) {
		String[] result = new String[3];
		String[] convertedfcNamesWithDays = fitnessClassNamesWithDays.toArray(new String[0]);
		JComboBox<String> fitnessClassList = new JComboBox<>(convertedfcNamesWithDays);
		JTextField customerIDText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Fitness Class and Day:"));
		myPanel.add(fitnessClassList);
		myPanel.add(new JLabel("Customer ID:"));
		myPanel.add(customerIDText);
		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			String[] parts = ((String) fitnessClassList.getSelectedItem()).split(" // ");
			result[0] = customerIDText.getText();
			result[1] = parts[0];
			result[2] = parts[1];
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}
	private String promptInputFitnessClassName(List<String> fitnessClassNames) {
		String result = new String();
		String[] convertedfcNames = fitnessClassNames.toArray(new String[0]);
		JComboBox<String> fitnessClassList = new JComboBox<>(convertedfcNames);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Fitness Class To Delete:"));
		myPanel.add(fitnessClassList);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Delete Fitness Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result =  (String) fitnessClassList.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}
	private String[] promptInputFitnessClassInfo() {
		String[] result = new String[6];
		JTextField fcNameText = new JTextField(5);
		JTextField locationText = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Fitness Class Name"));
		myPanel.add(fcNameText);
		myPanel.add(new JLabel("Location:"));
		myPanel.add(locationText);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Add Fitness Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == JOptionPane.OK_OPTION) {
			result[0] = fcNameText.getText();
			result[1] = locationText.getText();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	private String[] promptTablesJoin(List<String> allTableNames) {
		String[] result = new String[2];
		String[] convertedTableNames = allTableNames.toArray(new String[0]);
		JComboBox<String> tableNamesList = new JComboBox<>(convertedTableNames);
		JComboBox<String> tableNamesList2 = new JComboBox<>(convertedTableNames);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(new JLabel("Table 1:"));
		myPanel.add(tableNamesList);
		myPanel.add(new JLabel("Table 2:"));
		myPanel.add(tableNamesList2);

		int input = JOptionPane.showConfirmDialog(null, myPanel,
				"Join Tables", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == JOptionPane.OK_OPTION) {
			result[0] = (String) tableNamesList.getSelectedItem();
			result[1] = (String) tableNamesList2.getSelectedItem();
		}
		if (input == JOptionPane.CANCEL_OPTION) {
			result = null;
		}

		return result;
	}

	public static void main(String[] args) {
		RecreationCenterWindow recreationCenterWindow =new RecreationCenterWindow();
	}
	private void displayResult(List<String[]> res, JScrollPane scrollPane) {
		if (res.size() == 0) {
			return;
		}
		String[] columnNames = new String[res.get(0).length];
		String[][] data = new String[res.size() - 1][res.get(0).length];
		for (int i = 0; i < res.size(); i++) {
			for (int j = 0; j < res.get(0).length; j++) {
				if (i == 0) {
					columnNames[j] = res.get(0)[j];
				} else {
					data[i - 1][j] = res.get(i)[j];
				}
			}
		}
		// Initializing the JTable
		JTable jTable;
		jTable = new JTable(data, columnNames);
		jTable.setBounds(30, 40, 200, 300);
		scrollPane.setViewportView(jTable);
	}


	private void displayErrorMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}


}

package framework.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class AccountManager {

    private static final Vector accounts = new Vector();
    private static final String RECORD_STORE_ACCOUNT = "RECORD_STORE_ACCOUNT";

    public static void loadAccounts() {
        RecordStore rs = null;
        try {
            synchronized (accounts) {
                rs = RecordStore.openRecordStore(RECORD_STORE_ACCOUNT, true);
                RecordEnumeration re = rs.enumerateRecords(null, null, false);
                for (int i = 0; i < re.numRecords(); i++) {
                    int id = re.nextRecordId();

                    ByteArrayInputStream bis = new ByteArrayInputStream(rs.getRecord(id));
                    DataInputStream dis = new DataInputStream(bis);
                    String username = dis.readUTF();
                    String password = dis.readUTF();
                    dis.close();
                    dis = null;
                    bis.close();
                    bis = null;

                    accounts.addElement(new Account(id, username, password));
                }
            }
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreNotOpenException e) {
                e.printStackTrace();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateAccount(Account account) {
        if (account.getId() == Account.NOT_SAVED) {
            addAccount(account);
            return;
        }

        RecordStore rs = null;
        try {
            synchronized (accounts) {
                rs = RecordStore.openRecordStore(RECORD_STORE_ACCOUNT, true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(account.getUsername());
                dos.writeUTF(account.getPassword());

                byte[] data = bos.toByteArray();

                rs.setRecord(account.getId(), data, 0, data.length);

                dos.close();
                dos = null;
                bos.close();
                bos = null;
            }
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreNotOpenException e) {
                e.printStackTrace();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addAccount(Account account) {
        RecordStore rs = null;
        try {
            synchronized (accounts) {
                rs = RecordStore.openRecordStore(RECORD_STORE_ACCOUNT, true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(account.getUsername());
                dos.writeUTF(account.getPassword());

                byte[] recordData = bos.toByteArray();

                int id = rs.addRecord(recordData, 0, recordData.length);
                account.setId(id);

                dos.close();
                dos = null;
                bos.close();
                bos = null;

                accounts.addElement(account);
            }
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreNotOpenException e) {
                e.printStackTrace();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteAccount(int idx) {
        deleteAccount((Account)accounts.elementAt(idx));
    }

    public static void deleteAccount(Account account) {
        if (account.getId() == Account.NOT_SAVED) {
            synchronized (accounts) {
                accounts.removeElement(account);
            }
            return;
        }

        RecordStore rs = null;
        try {
            synchronized (accounts) {
                rs = RecordStore.openRecordStore(RECORD_STORE_ACCOUNT, true);
                rs.deleteRecord(account.getId());
                accounts.removeElement(account);
            }
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreNotOpenException e) {
                e.printStackTrace();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getAccountCount() {
        synchronized (accounts) {
            return accounts.size();
        }
    }

    public static Account getAccount(int i) {
        synchronized (accounts) {
            return (Account) accounts.elementAt(i);
        }
    }
}

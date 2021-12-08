package com.dony.dramatest

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.dony.dramatest.data.local.AppDatabase
import com.dony.dramatest.util.TestUtil
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var mDatabase: AppDatabase

    @Before
    fun createDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase::class.java)
            .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun isUserListEmpty() {
        Assert.assertEquals(0, mDatabase.userDao.loadAll().size)
    }

    @Test
    @Throws(Exception::class)
    fun insertUser() {
        Assert.assertNotNull(mDatabase.userDao.insert(TestUtil.createUser(0)))
    }

    @Test
    @Throws(Exception::class)
    fun retrievesUsers() {
        with(TestUtil.createUserList(5)) {
            forEach {
                mDatabase.userDao.insert(it)
            }
            Assert.assertEquals(this, mDatabase.userDao.loadAll())
        }
    }


    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        with(mDatabase.userDao) {
            deleteUser(TestUtil.createUser(8))
            Assert.assertNull(getUser(8))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllUsers() {
        with(mDatabase.userDao) {
            deleteAll()
            assert(loadAll().isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun search() {
        with(mDatabase.userDao) {
            with(TestUtil.createUserList(8)) {
                forEach {
                    insert(it)
                }
                Assert.assertEquals(8, searchUsers("%God%").size)
            }
        }
    }
}
// -*- c-file-style: "bsd" -*-

#ifndef _LIB_MESSAGE_H_
#define _LIB_MESSAGE_H_

#include <stdarg.h>
#include <stdbool.h>
#include <stdio.h>

enum Message_Type {
        MSG_PANIC = 0,
        MSG_WARNING,
        MSG_NOTICE,
        MSG_DEBUG,
        MSG_NUM_TYPES,
        MSG_PERROR = 1 << 16,
};

#define PanicFlags(flags, msg...)                                       \
        do {                                                            \
                _Message(MSG_PANIC|flags, __FILE__, __LINE__, __func__, msg); \
                _Panic();                                               \
        } while (0)
#define MessageFlags(flags, msg...)                             \
        _Message(flags, __FILE__, __LINE__, __func__, msg)

#define Panic(msg...)   PanicFlags(0, msg)
#define Warning(msg...) MessageFlags(MSG_WARNING, msg)
#define Notice(msg...)  MessageFlags(MSG_NOTICE, msg)
#define QNotice(msg...) _Message(MSG_NOTICE, NULL, 0, NULL, msg)

#define PPanic(msg...)   PanicFlags(MSG_PERROR, msg)
#define PWarning(msg...) MessageFlags(MSG_WARNING|MSG_PERROR, msg)
#define PNotice(msg...)  MessageFlags(MSG_NOTICE|MSG_PERROR, msg)

void _Message(enum Message_Type type,
              const char *fname, int line, const char *func,
              const char *fmt, ...)
        __attribute__((format(printf,5,6)));
void _Panic(void) __attribute__((noreturn));
bool _Message_DebugEnabled(const char *fname);

void Message_VA(enum Message_Type type,
                const char *fname, int line, const char *func,
                const char *fmt, va_list args);

void _Message_VA(enum Message_Type type, FILE *fp,
                 const char *fname, int line, const char *func,
                 const char *fmt, va_list args);

const char *Message_DFree(char *buf);
void Message_DoFrees(void);

void _Message_Hexdump(const void *data, int len);
char *Message_FmtBlob(const void *data, int len);
void PanicOnSignal(int signo);

// This is not a mistake.  We actually want exactly one of these flags
// per file that uses the Debug macro.
static __attribute__((unused)) signed char _Message_FileDebugFlag = -1;

#define Debug(msg...)                                   \
        do {                                            \
                if (Message_DebugEnabled(__FILE__))     \
                        MessageFlags(MSG_DEBUG, msg);   \
        } while (0)
#define Message_Hexdump(data, len)                      \
        do {                                            \
                if (Message_DebugEnabled(__FILE__))     \
                        _Message_Hexdump(data, len);    \
        } while (0)
#define Assert(pred)                                            \
        do {                                                    \
                if (!(pred))                                    \
                        Panic("Assertion `%s' failed", #pred);  \
        } while (0)

static inline bool
Message_DebugEnabled(const char *fname)
{
        if (_Message_FileDebugFlag >= 0)
                return _Message_FileDebugFlag;
        _Message_FileDebugFlag = _Message_DebugEnabled(fname);
        return _Message_FileDebugFlag;
}

#include "hash.h"

#define FMT_BLOB "<%ld %08x>"
#define VA_BLOB(d, l) (long)l, hash(d, l, 0)

#define FMT_VBLOB "%s"
#define XVA_VBLOB(d, l) Message_DFree(Message_FmtBlob(d, l))

#endif // _LIB_MESSAGE_H_
